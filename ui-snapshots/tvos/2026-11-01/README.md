# tvOS / Apple TV UIスナップショット — 2026年11月1日

[全プラットフォーム](../../README.md) · [Apple 4版の比較入口](../../apple-2026-11-01.md) · [メタデータ](manifest.json) · [撮影時刻・フォーカス記録](capture-details.json) · [撮影結果](capture-result.txt)

固定コミットの実UIをサンプルデータで撮影した **12枚**です。生成モックではありません。見た目の記録であり、仕様の採用・準拠や実機動作の保証とは区別します。

## 収録条件

| 項目 | 値 |
|---|---|
| 撮影日時 | 2026-11-01T00:26:40+09:00 〜 2026-11-01T00:28:01+09:00（日本時間） |
| 参照版 | [a4c4040](https://github.com/mnagata/ContinuousPlayer_for_iOS/commit/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5) / Debug / 1.0 (1) |
| 機種・OS | Apple TV 4K (3rd generation) (at 1080p) / tvOS 27.0 (24J360) |
| ビルド環境 | Xcode 27.1 (27A9275)、arm64。製品ソースは固定コミットと一致 |
| 入力・撮影 | XCUIRemoteによる方向・Select・Play/Pause。XCUIScreen.main.screenshot → XCTAttachment → xcresulttool export attachments（PNGをそのままコピー） |
| 言語・文字 | アプリja / ja_JP、標準文字。OS全体の優先言語は未測定 |
| 寸法・ハッシュ | 画像ごとの格納px寸法・表示px寸法・EXIF向き・SHA-256をmanifestへ記録 |

## サンプルと画面遷移

640×360・24fps・90秒のtestsrc2動画（音声なし）、48kHz・mono無音AACのみのMP4（90秒 / 2秒）、意図的な破損MP4、空の子フォルダー、同名異形式M4V/MP4。

ホーム → 保存済み → フォルダーを追加 → DLNAサーバー → UI Sample NAS → このフォルダーを登録 → 保存済み行 → ファイル一覧 → 再生、の通常UI経路で撮影しました。サーバー接続先とテスト登録の初期化だけを起動引数で準備し、プレイヤーの状態や画面を注入していません。

動画はSpace / Play-Pauseで一時停止して情報を開きます。音声・終了・エラーは接続先をaudio.xml / end.xml / failure.xmlへ切り替えた別キューです。登録は`uitests.`専用キーですが、DLNA最終接続先は共通設定キーをテストURLへ更新します。

リストの明るい行と再生・閉じる・再試行の強調がリモコンのフォーカス表示です。`capture-details.json`の空文字は、測定対象のbuttons/cells/textFieldsにフォーカスがなかったことを示します。映像入力surfaceのフォーカスまでは測定していません。音声情報の自動フォーカス移動は到達できず、未収録です。

## ホーム

### ホーム / tv-focus

`home/default` · PNG格納 1920×1080px · 表示 1920×1080px

![ホーム / tv-focus](images/home--default--tv-focus.png)

ロゴ、ANIME OPENINGS / ENDINGS、ContinuousPlayer、「動画フォルダーを選ぶ」を表示する。

測定フォーカス: home.chooseFolder:動画フォルダーを選ぶ

## フォルダーの選択・登録

### フォルダーの追加先 / tv-focus

`add-source/default` · PNG格納 1920×1080px · 表示 1920×1080px

![フォルダーの追加先 / tv-focus](images/add-source--default--tv-focus.png)

保存元の選択画面。tvOSはDLNA、ほかの3版は端末・外部ストレージとDLNAの入口を表示する。

測定フォーカス: :DLNAサーバー、NASのフォルダーを開いて登録

### DLNAサーバー・サンプル接続済み / tv-focus

`dlna-servers/populated` · PNG格納 1920×1080px · 表示 1920×1080px

![DLNAサーバー・サンプル接続済み / tv-focus](images/dlna-servers--populated--tv-focus.png)

UI Sample NAS、ループバックの接続先、接続操作を表示する。自動探索は撮影引数で無効化している。

測定フォーカス: :UI Sample NAS

### 保存済みフォルダー・未登録 / tv-focus

`saved-folders/empty` · PNG格納 1920×1080px · 表示 1920×1080px

![保存済みフォルダー・未登録 / tv-focus](images/saved-folders--empty--tv-focus.png)

未登録の案内と「フォルダーを追加」を表示する。

測定フォーカス: saved.addFolder:フォルダーを追加

### 保存済みフォルダー・登録あり / tv-focus

`saved-folders/populated` · PNG格納 1920×1080px · 表示 1920×1080px

![保存済みフォルダー・登録あり / tv-focus](images/saved-folders--populated--tv-focus.png)

DLNAフォルダーにUI Sample NASの登録行を表示する。

測定フォーカス: saved.dlna.uuid:ui-sample-video / 0:UI Sample NAS、UI Sample NAS · UI Sample NAS

## ファイル一覧

### 再生開始ファイルの選択 / tv-focus

`file-browser/populated` · PNG格納 1920×1080px · 表示 1920×1080px

![再生開始ファイルの選択 / tv-focus](images/file-browser--populated--tv-focus.png)

Other Folderと同名Sample OPの2形式を表示し、M4V / MP4の形式バッジで区別する。

測定フォーカス: dlna.folder.empty:Other Folder

## 再生画面

### 音声のみの再生中 / tv-audio-surface

`player/audio-playing` · PNG格納 1920×1080px · 表示 1920×1080px

![音声のみの再生中 / tv-audio-surface](images/player--audio-playing--tv-audio-surface.png)

黒背景中央に波形アイコンを表示し、再生中も上下の操作部を表示する。

測定フォーカス: 測定したボタン・行・入力欄には該当なし（映像surfaceは測定対象外）

### フォルダーの再生終了 / tv-focus

`player/ended` · PNG格納 1920×1080px · 表示 1920×1080px

![フォルダーの再生終了 / tv-focus](images/player--ended--tv-focus.png)

Short Audioとフォルダーの再生終了メッセージ、再生ボタンを表示する。

測定フォーカス: player.toggle:再生

### 動画一時停止 / tv-focus

`player/video-paused` · PNG格納 1920×1080px · 表示 1920×1080px

![動画一時停止 / tv-focus](images/player--video-paused--tv-focus.png)

上端にタイトルとホーム・一覧・情報、下端に前・−10秒・再生・＋10秒・次の5操作を表示する。選択したMP4は2件キューの末尾で、次は無効。

測定フォーカス: player.toggle:再生

### 動画再生中 / tv-video-surface

`player/video-playing` · PNG格納 1920×1080px · 表示 1920×1080px

![動画再生中 / tv-video-surface](images/player--video-playing--tv-video-surface.png)

640×360・24fpsの合成テスト映像を表示する。再生操作部は隠れている。

測定フォーカス: 測定したボタン・行・入力欄には該当なし（映像surfaceは測定対象外）

## メディア情報

### 動画のメディア情報 / tv-focus

`media-info/video` · PNG格納 1920×1080px · 表示 1920×1080px

![動画のメディア情報 / tv-focus](images/media-info--video--tv-focus.png)

元のファイル名Sample OP.mp4、サイズ、avc1・640×360・24fps、音声なしを表示する。 撮影位置は一覧の上部で、音声出力欄は未収録。

測定フォーカス: player.info.close:閉じる

## 再生失敗

### 破損メディアの再生失敗 / tv-focus

`playback-error/decode` · PNG格納 1920×1080px · 表示 1920×1080px

![破損メディアの再生失敗 / tv-focus](images/playback-error--decode--tv-focus.png)

Corrupt OP.mp4に対して「再生できません」「開けません」、再試行・ファイル一覧・閉じるを表示する。失敗原因の分類や復旧成功は画像だけでは判定しない。

測定フォーカス: player.error.retry:再試行

## 未収録・対象外

| 対象 | 状態 | 理由 |
|---|---|---|
| `system-picker/default,permission/default` | pending | OSファイル選択・権限案内・USBストレージ接続は収録していない。 |
| `dlna-servers/empty,manual-entry,connection-error` | pending | DLNAはlocalhostへの接続済み画面のみ。自動探索・実LANの失敗・入力中は未収録。 |
| `file-browser/loading,failure` | pending | 撮影用サーバーは即時応答。読み込み中・一覧取得失敗は未収録。 |
| `accessibility,additional-window-sizes,real-devices` | pending | 大きな文字、VoiceOver、別サイズ、実機、実NAS、実USB DAC、中断復帰の表示は未収録。 |
| `saved-folders/remove-confirmation,file-browser/empty` | pending | この撮影ハーネスでは未収録。Mac Catalystの画像を代用しない。 |
| `media-info/audio` | pending | 撮影ハーネスがフォーカス移動で到達できなかった。音声のみの再生画面は収録済み。 |
| `media-info/video-output-section` | pending | メディア情報の下部にスクロールした音声出力欄は未収録。 |
| `system-picker/local-folder` | not-applicable | この固定コミットのtvOS追加先はDLNAのみ。端末フォルダー入口を持たない。 |

未収録は未実装を意味しません。撮影の制約は[manifest.json](manifest.json)の`limitations`にも記録しています。

## 再現と根拠

- [Apple版の再撮影手順](../../../tools/apple/README.md)
- [共通操作仕様](../../../docs/common-operation-spec.md)
- [固定コミットの画面定義](https://github.com/mnagata/ContinuousPlayer_for_iOS/tree/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS)
- [固定コミットのtvOS画面定義](https://github.com/mnagata/ContinuousPlayer_for_iOS/tree/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_tvOS)
