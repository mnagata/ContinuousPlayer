using Windows.Media.Editing;
using Windows.Media.MediaProperties;
using Windows.Media.Transcoding;
using Windows.Storage;

if (args.Length != 1) throw new ArgumentException("Specify a new output directory.");
var root = Path.GetFullPath(args[0]);
if (Directory.Exists(root) && Directory.EnumerateFileSystemEntries(root).Any())
    throw new IOException("Output directory must be empty; existing samples are not overwritten.");
var sample = Path.Combine(root, "Sample");
var end = Path.Combine(root, "End");
Directory.CreateDirectory(Path.Combine(sample, "Empty"));
Directory.CreateDirectory(end);

var folder = await StorageFolder.GetFolderFromPathAsync(sample);
var video = await folder.CreateFileAsync("Sample OP.mp4", CreationCollisionOption.FailIfExists);
var composition = new MediaComposition();
composition.Clips.Add(MediaClip.CreateFromColor(
    Windows.UI.Color.FromArgb(255, 16, 23, 67), TimeSpan.FromSeconds(120)));
var result = await composition.RenderToFileAsync(video, MediaTrimmingPreference.Precise,
    MediaEncodingProfile.CreateMp4(VideoEncodingQuality.Qvga));
if (result != TranscodeFailureReason.None) throw new IOException(result.ToString());

WriteSilentWave(Path.Combine(sample, "Sample ED.wav"), 90);
WriteSilentWave(Path.Combine(end, "End.wav"), 2);
await File.WriteAllTextAsync(Path.Combine(sample, "Corrupt.mp4"),
    "Intentionally invalid UI snapshot sample\n");
Console.WriteLine(root);

static void WriteSilentWave(string file, int seconds)
{
    const int sampleRate = 48000;
    var bytes = sampleRate * 2 * seconds;
    using var stream = new BinaryWriter(File.Open(file, FileMode.CreateNew));
    stream.Write("RIFF"u8);
    stream.Write(36 + bytes);
    stream.Write("WAVEfmt "u8);
    stream.Write(16);
    stream.Write((short)1);
    stream.Write((short)1);
    stream.Write(sampleRate);
    stream.Write(sampleRate * 2);
    stream.Write((short)2);
    stream.Write((short)16);
    stream.Write("data"u8);
    stream.Write(bytes);
    stream.Write(new byte[bytes]);
}
