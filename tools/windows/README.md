# Windows UIスナップショット撮影

[収録記録](../../ui-snapshots/windows/2026-11-03/README.md) · [記録形式](../../ui-snapshots/format.md)

撮影対象はWindows版の通常UIです。サンプル登録の準備だけを保存ファイルで行い、再生・停止・終了・エラーをコードから注入しません。通常版と保存領域を分けるため、撮影用のパッケージIDを使用します。

## サンプルを作る

Windowsと.NET 10以上のSDKを使います。出力先には新しい撮影専用ディレクトリを指定してください。

```powershell
dotnet run --project tools/windows/FixtureGenerator -- C:\Capture\media
```

`Sample`には単色の120秒MP4、90秒の無音PCM WAV、意図的な破損MP4と空の`Empty`フォルダー、`End`には2秒の無音WAVを作ります。既存ファイルがある出力先では停止します。動画はWindows.Media.EditingのMediaCompositionで生成するため、Windows環境によって符号化結果が変わる可能性があります。

2026-11-03の撮影では、同じ方法で以前生成していた`Cursor test.mp4`を`Sample OP.mp4`として複写しました。実際の動画はH264・320×240・15fps / AAC・44.1kHz・2chで、SHA-256は収録manifestに記録しています。WAVは48kHz・mono・16-bitのゼロ値PCMです。メディア本体は共通リポジトリへ追加しません。

## ビルドと専用パッケージ

参照コミットと作業差分を記録し、Windows版リポジトリで実行します。2026-11-03の参照版は`5aeed393d93c09a4a572bd3d9c01263f2bb538aa`です。WinApp CLI 0.7.1を使用しました。

```powershell
winapp run .\ContinuousPlayer --no-restore --no-launch --json
```

ビルド済みフォルダーを新しい撮影用ディレクトリへ複写します。既存の`AppX`ディレクトリを再帰的に複写しないでください。製品ソースは変更せず、複写先の生成済み`AppxManifest.xml`の`Identity Name`だけを`ContinuousPlayer.UISnapshots`へ変更します。PhoneIdentityやPublisherは変更しません。

```powershell
$buildRoot = "$PWD\ContinuousPlayer\bin\x64\Debug\net10.0-windows10.0.26100.0\win-x64"
$captureRoot = "$PWD\.tools\ui-capture-<新しい撮影ID>"
$layout = "$captureRoot\IsolatedAppX"
New-Item -ItemType Directory -Path $layout -ErrorAction Stop | Out-Null
Get-ChildItem -LiteralPath $buildRoot | Where-Object Name -ne 'AppX' |
    Copy-Item -Destination $layout -Recurse
$manifestPath = "$layout\AppxManifest.xml"
[xml]$manifest = Get-Content -LiteralPath $manifestPath -Raw
$manifest.Package.Identity.Name = 'ContinuousPlayer.UISnapshots'
$manifest.Save($manifestPath)
Add-AppxPackage -Register $manifestPath
winapp run $layout --manifest $manifestPath --output-appx-directory "$captureRoot\CaptureAppX" --detach --json
```

プロジェクトモードの`--manifest`だけでは、ビルド済みレイアウトに元のIDが残る場合があります。撮影前に登録済みパッケージと複写後のmanifestのIDが専用IDであることを確認します。初回の登録0件でホーム・保存済み・追加先を撮影してください。

専用アプリを終了したあと、専用IDの`LocalState/library.json`へ次の形式で2件だけを準備し、同じパッケージを再起動します。通常版の保存領域には書き込みません。

```json
{
  "Version": 1,
  "Folders": [
    { "Kind": "local", "Name": "Sample Media", "Path": "C:\\Capture\\media\\Sample" },
    { "Kind": "local", "Name": "End Sample", "Path": "C:\\Capture\\media\\End" }
  ],
  "LastFolderKey": null,
  "LastMediaId": null,
  "LastPlaybackFolder": null
}
```

2026-11-03に使用した保存先は`%LOCALAPPDATA%\Packages\ContinuousPlayer.UISnapshots_1z32rh13vfry6\LocalState\library.json`です。Publisherが異なる場合はパッケージファミリー名も変わります。

## 撮影・保存・後片付け

1. `manifest.json`に記録した各画像の`steps`に従い、通常UIを操作します。画面遷移とアニメーションが終わってから撮影し、ツールチップ、読込途中、文字切れを目視確認します。
2. 2026-11-03はComputer Useの`sky.get_window_state`が返すウィンドウJPEGを保存しました。最初のウィンドウ画像全体を使用し、タイトルバーとダイアログを含めます。ポインターと青い強調表示もそのまま残しています。
3. JPEG原本を`source-jpeg/`へ保存し、`System.Drawing.Image.Save(path, ImageFormat.Png)`で同寸法PNGを`images/`へ作ります。リサイズ・切り抜き・注釈・合成はしません。両形式のハッシュ、PNG寸法、撮影条件をmanifestへ記録します。
4. 登録解除確認は「閉じる」、音声設定は「キャンセル」で閉じ、撮影用アプリを終了します。撮影用パッケージだけを削除し、通常版の登録を標準のビルド先へ戻します。

```powershell
Get-AppxPackage -Name ContinuousPlayer.UISnapshots | Remove-AppxPackage
winapp run .\ContinuousPlayer --no-build --no-launch --json
```

通常版の登録・設定・個人メディアは撮影に使用しません。実NAS、USB、外部DAC、排他再生、全画面、文字倍率、フォーカス比較などを収録していない場合は`coverage`に残します。

共通リポジトリで画像・原本・ハッシュ・ID・リンクを検証します。

```powershell
pwsh -File tools/windows/Test-Capture.ps1
```
