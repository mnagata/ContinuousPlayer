package jp.nagu.continuousplayer

/**
 * 動画ファイルのメタデータを保持するデータクラス。
 *
 * @property uri 動画のコンテンツURI文字列（SAF document URI）
 * @property displayName 表示用ファイル名
 * @property size ファイルサイズ（バイト）
 * @property lastModified 最終更新日時（エポックミリ秒）
 */
data class VideoItem(
    val uri: String,
    val displayName: String,
    val size: Long,
    val lastModified: Long
)
