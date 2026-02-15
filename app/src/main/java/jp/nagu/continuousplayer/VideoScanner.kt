package jp.nagu.continuousplayer

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import java.text.Collator
import java.util.Locale

/**
 * SAF (Storage Access Framework) のツリーURIを使って動画ファイルをスキャンするクラス。
 *
 * [DocumentFile] を使用してフォルダ内のファイルを列挙し、
 * `.mp4` / `.m4v` 拡張子のファイルを [Collator.SECONDARY] でロケール順にソートして返す。
 * 内部ストレージ・USBストレージ等のストレージ種別を問わず動作する。
 */
class VideoScanner(private val context: Context) {

    companion object {
        private const val TAG = "VideoScanner"
    }

    private val collator: Collator = Collator.getInstance(Locale.getDefault()).apply {
        strength = Collator.SECONDARY
    }

    /**
     * 指定されたツリーURI配下の動画ファイル（.mp4, .m4v）を取得する。
     *
     * @param treeUri SAF OpenDocumentTree で取得したフォルダのツリーURI
     * @return ファイル名でソートされた [VideoItem] のリスト。フォルダが無効な場合は空リスト
     */
    fun scanTree(treeUri: Uri): List<VideoItem> {
        Log.d(TAG, "scanTree: treeUri=$treeUri")
        val tree = DocumentFile.fromTreeUri(context, treeUri)
        if (tree == null) {
            Log.d(TAG, "scanTree: fromTreeUri returned null")
            return emptyList()
        }
        return tree.listFiles()
            .filter { it.isFile && it.name?.let { name ->
                name.endsWith(".mp4", ignoreCase = true) ||
                    name.endsWith(".m4v", ignoreCase = true)
            } == true }
            .sortedWith(compareBy(collator) { it.name ?: "" })
            .map { doc ->
                VideoItem(
                    uri = doc.uri.toString(),
                    displayName = doc.name ?: "unknown",
                    size = doc.length(),
                    lastModified = doc.lastModified()
                )
            }
    }
}
