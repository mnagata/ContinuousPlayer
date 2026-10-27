# ContinuousPlayer：現行Android版を基準にしたUI・操作比較

調査日：2026年10月27日（日本時間）

Androidは一時停止メニューの上下端への接地まで反映した実装を対象とする。iOS・Windowsは確認できたローカルの参照コミットを対象とする。共通操作仕様の内容を、各版の実装済み機能として扱わない。

[2026年10月24日の比較資料](platform-ui-operation-comparison-2026-10-24.md)は当時の記録として残す。本書は、その後のAndroidの変更を反映した新しい比較資料である。

## 1. 調査対象と確認方法

| 版 | 参照コミット | コミット日時（日本時間） | 前回比較からの変更 |
|---|---|---|---|
| Android・Android TV | [268d99c](https://github.com/mnagata/ContinuousPlayer_for_Android/commit/268d99cbc2a3de2662210229dc7c4c267dcde375) | 2026-10-27 18:17 | 更新あり |
| iOS・iPadOS・Mac Catalyst・tvOS | [8ec1593](https://github.com/mnagata/ContinuousPlayer_for_iOS/commit/8ec1593e2cd5724158834c6d305c3b11cdd8906d) | 2026-10-23 23:13 | 同じ参照コミット |
| Windows | [6280695](https://github.com/mnagata/ContinuousPlayer_for_Win/commit/62806952a521cada0e6b018734041c3768ceba81) | 2026-10-18 16:22 | 同じ参照コミット |

画面定義、入力処理、ファイル選択、再生状態の処理を静的に照合した。文書とコードが食い違う場合はコードを優先した。特にAndroid READMEには単一SAF登録時の一覧省略など旧手順が残っているため、その記載を現行動作の根拠にはしていない。

本書作成時には3版を並べた実画面比較や実機入力・NAS・USB DACの検証を行っていない。寸法はソースの指定値であり、dp・sp・pt・DIPの実際の見え方が同じであることを意味しない。GitHubの最新mainの継続追跡ではなく、上表の固定版同士の比較である。

## 2. 比較結果の要点

Androidは、再選択時のセッション保持、登録後の保存済み一覧への復帰、共通キー割当、手入力のDLNA接続、失敗時の停止に対応した。一時停止メニューは上端・下端の各1行に整理され、アプリ内終了と重複する補助メニューも廃止された。

一方、iOSは選び直し後の一時停止維持、通常エラーの自動スキップ、縦画面の専用情報領域を持つ。Windowsは上部の一時停止ツールバー、再選択時のプレイヤー解放、左右クリックでのシークを維持している。前回の「AndroidとWindowsが近い」という総括だけでは、現在の操作差を説明できない。

## 3. ホームとアプリ終了

| 項目 | Android | iPhone／iPad | Windows |
|---|---|---|---|
| ホーム | ロゴ、「ホーム」、アプリ名、「OP / EDを選ぶ」。TVのみ操作案内 | ロゴ、英字ラベル、アプリ名、選択、補足説明 | ロゴ、英字ラベル、アプリ名、選択、Exit、キー案内 |
| ロゴの表示領域 | 通常180dp、横画面100dp | 通常200pt、高さ450pt未満では120pt | 180DIP |
| 選択ボタン | 通常幅280dp・最小高さ56dp | 最大幅320pt、上下18ptの余白 | 280×56DIP |
| アプリ内終了 | ホーム・再生画面とも設けない。ホームの戻るはOSへ渡す | 設けない | ホームと再生ツールバーに終了ボタンあり |
| 共通する意匠 | 濃紺、シアン・紫、重なるフレームと再生マーク | 同じモチーフ。配色値・字体・角丸は別指定 | Android由来の配色・図形をXAMLで実装 |

Androidの「その他」→「アプリを終了」は廃止済み。Windowsに終了ボタンが残っていることは、現在の共通仕様との差である。

根拠：[Android画面][a-layout]・[寸法][a-dimens]・[横画面寸法][a-land-dimens]、[iOSホーム][i-content]、[Windows画面][w-layout]・[終了処理][w-main]。

## 4. 保存済みフォルダーとファイル選択

| 項目 | Android | iPhone／iPad | Windows |
|---|---|---|---|
| ホームからの入口 | 保存済みが0件・1件・複数件でも必ず一覧を表示 | 保存済み一覧を表示 | 保存済み一覧を表示 |
| 登録直後 | SAF・USB・DLNAとも保存済み一覧の登録行へ戻る。登録だけでは再生しない | 標準フォルダー選択等で登録し、保存済み一覧から開く | 登録操作では保存済み一覧へ戻る。追加用ブラウザーから登録前に再生する経路もある |
| ブラウザー | 暗色の共通ダイアログ。最大幅880dp、高さは利用可能領域92%・最大800dp | SwiftUIの一覧・NavigationStack。通常最大880×800pt | 暗色の共通オーバーレイ。最大880×800DIP |
| ファイル行 | 20sp、縦最大2行・最小48dp、横1行・最小40dp | 標準文字サイズ、最大3行。通常最小44pt、iPhone横画面40pt | 20DIP、最大2行、最小52DIP |
| 一覧の拡張子 | 対応する末尾拡張子を省略。DLNAにも適用 | ローカルは省略。DLNAはサーバーのtitleを表示 | ローカルは拡張子付き。DLNAはサーバーのtitleを表示 |
| 同名になる項目 | 拡張子省略後の衝突に形式バッジを付ける | Androidと同じ形式バッジ処理ではない | Androidと同じ形式バッジ処理ではない |
| 登録解除 | 保存済み行の長押し・右クリックで確認。実ファイルは削除しない | 長押し・左スワイプ | Delete・右クリック・長押し・解除ボタンで確認 |
| 読み込み | 既存一覧を維持し、SAF・USB・DLNAで0.5秒後に遅延表示。初回読み込みは別扱い | 読み込み状態へ表示を切り替える | 既存一覧を維持。DLNAは0.5秒後に遅延表示 |

Androidでは次の補助操作を廃止している。

- 保存済み行の「その他」ボタンと登録解除用ポップアップ。登録解除は長押し・右クリックから行う。
- 「フォルダーを追加」の保存先選択画面の「保存済みフォルダー」ボタン。キャンセル・戻る・Escで一覧へ戻る。
- ファイル一覧の「名前・形式」ボタンと、ファイル行の長押し・右クリックから開くメディア情報。これらの操作で再生も始めない。詳細は再生一時停止中の「メディア情報」で確認する。

形式バッジ、元の名前・形式のツールチップと読み上げは維持する。ファイルの長押しと、保存済みフォルダーの登録解除を混同しない。

根拠：[Android保存済み・登録フロー][a-saf]・[共通ダイアログ][a-browser]・[表示名と形式][a-item]、[iOSローカル一覧][i-browser]・[DLNA一覧][i-dlna]・[共通スタイル][i-browser-style]、[Windows一覧処理][w-browser]・[画面][w-layout]。

## 5. 一時停止中の画面・メディア情報

| 項目 | Android | iPhone／iPad | Windows |
|---|---|---|---|
| 上部 | 1行。拡張子を除いたタイトルが左、ホーム・ファイル一覧・メディア情報の3アイコンが右 | タイトル行と操作ボタン行。ホーム・選び直し・情報・再生／一時停止 | 高さ56DIPの1行。ホーム・名前・再開・情報・フォルダー・終了 |
| 再生コントロール | 下端中央に前・−10秒・再生／一時停止・＋10秒・次の5ボタン | 情報パネル内に前・−10秒・再生・＋10秒・次を表示 | 専用の前後・シーク画面ボタンはない |
| 画面端との関係 | 上下の背景を画面端に接地。非表示のシステムバーの高さを外側に予約しない | SwiftUIの画面・情報パネルとして配置 | 上部ツールバーと下部情報・状態文として配置 |
| 長いタイトル | 左側の残り幅で1行・末尾省略。アイコンを押し出さない | 上部タイトルは1行・中央省略 | ツールバー内で省略 |
| 縦画面 | 操作部の表示中は重複する補助情報を隠す。操作部のために映像領域を変えない | 再生・一時停止の両状態で高さ35%の情報領域を確保 | 縦長ウィンドウでは映像上に下部情報を重ねる |
| 音声のみ | タイトル・操作部を常時表示 | 中央に波形マーク | 中央に音声アイコンとファイル名 |
| 終了・エラー | 操作部を表示し、上端の同じ行内に状態文を表示 | 状態と操作部を表示 | 状態文を表示。エラーはダイアログで案内 |
| 詳細情報 | ファイル→映像・音声形式→音声出力の順。元の名前を保持 | セクション付きの情報シート | スクロール可能な再生情報ダイアログ |

Androidでは、表示中のシステムバーや切り欠きを避ける余白をメニューの内側に加える。背景は上端・下端に接したまま、下段の高さを必要に応じて広げる。各ボタンは48dp。上下のフォーカス移動とTab順も、上部3アイコン・下部5ボタンに対応する。

AndroidとiPhone／iPadには画面上の前後・シーク操作があるが、配置は一致しない。Windowsの「上部だけの操作部」を現行Androidと同じ配置とは記述しない。

根拠：[Android画面][a-layout]・[Insets・状態更新][a-main]・[現行仕様][a-spec]、[iOS再生画面][i-player]、[Windows画面][w-layout]・[再生処理][w-player]。

## 6. 選び直し・キャンセル・割り込み

| 場面 | Android | iPhone／iPad | Windows |
|---|---|---|---|
| 再生中に一覧を開く | 同じプレイヤー・キュー・位置・再生意図を保持して一時停止 | 一時停止して動画上に一覧を重ねる | プレイヤーを解放し、再生元の一覧へ移る |
| 一覧をキャンセル | 元の位置と再生／一時停止状態へ戻る。割り込み後やアクセス失効時は自動再開しない | 元の動画の一時停止画面へ戻る | 元の位置へ戻るセッション保持経路はない |
| 再生から開いた一覧でファイルを確定 | 同じファイルでも0秒から新しいセッションで自動再生 | ローカル・DLNAとも再選択ではautoplay=false。一時停止状態で切り替える | 選択先を0秒から再生 |
| 再生画面の戻る／Esc | スマホ・TVの戻るとEsc／Oは、現在ファイルを指定して一覧を開く | iPhone／iPadの再生面にはEscの一覧表示割当なし。Macは対応 | Esc／Oで再生を停止して再生元一覧へ |
| ホーム操作 | 再生セッションを終了してホーム | 再生を終了してホーム | 再生を終了してホーム |
| バックグラウンド・ロックから復帰 | 元のファイル・位置で一時停止し、明示再開を待つ | 一時停止し、手動再開 | 非アクティブ化で一時停止し、自動再開しない |
| 通常のウィンドウフォーカス喪失 | それだけでは再生を停止しない | iPhone／iPadはアプリ非アクティブ化で停止。Macは後述 | ウィンドウ非アクティブ化で停止 |
| 再起動後 | 登録先・最後の再生元フォルダーを利用。秒数・キューは永続復元しない | 登録先等を保持。秒数は永続復元しない | 登録先・再生元・ファイルIDを保存。秒数・キューは復元しない |

Androidの「一覧キャンセルによる再開」と「割り込み後の手動再開待ち」は別の規則である。iOSの位置保持とAndroidの位置保持を、キャンセル後の再生意図まで同じ動作として扱わない。

根拠：[Android保持・復元・入力処理][a-main]、[iOSローカル再選択][i-content]・[DLNA再選択][i-dlna]・[仕様][i-spec]、[Windows一覧復帰][w-player]・[仕様][w-spec]。

## 7. 入力機器別の比較

再生領域が操作対象の場合の割当を示す。Androidではフォーカス中のボタン・一覧・文字入力を優先する。

| 操作 | Android | iPhone／iPad | Windows |
|---|---|---|---|
| タッチの左／中央／右1/3 | −10秒／再生切替／＋10秒 | 同じ。映像領域が対象 | 同じ |
| マウスの背景クリック | 場所によらず再生切替。近い位置のダブルクリックは1回分 | 主にタッチUI。Macのクリックは後述 | 左／右でシーク、中央で再生切替。解放ごとに処理 |
| ←／→ | 前／次 | 前／次 | 前／次 |
| ↑／↓ | −10秒／＋10秒 | −10秒／＋10秒 | −10秒／＋10秒 |
| 再生切替キー | Space・Enter・TV決定・メディアキー | Space | Space・Enter・メディアキー |
| Shift＋←／→ | −10秒／＋10秒 | −10秒／＋10秒 | 専用の修飾キー割当なし |
| 情報 | I・INFO | I | I |
| 一覧・ホーム | Esc／Oで一覧、Hでホーム | iPhone／iPadの再生面には同じ割当なし | Esc／Oで一覧、Hでホーム |
| 一時停止ボタン間の移動 | 方向キー・Tab／Shift＋Tab | 各UIと外部キーコマンドによる | Tab／Shift＋Tab。矢印は前後・シークを維持 |
| 動画のポインター無操作 | 3秒で操作部とカーソルを隠す。ボタン上・フォーカス中等は維持 | Androidと同じポインター用制御ではない | 3秒でカーソルを隠す。操作部は一時停止状態で表示 |

### タップ・スワイプの判定

| 項目 | Android | iPhone／iPad | Windows |
|---|---|---|---|
| 右／左スワイプ | 前／次 | 前／次 | 前／次 |
| 横スワイプの成立条件 | 横70dp以上かつ横が縦の1.5倍以上。境界を含み、速度条件なし | 横70pt超かつ横が縦の1.5倍超。速度条件なし | 横100DIP超、横が縦より大きく、横速度100DIP/秒超 |
| ダブルタップ | 初回の指離しで実行し、近接する同領域の追加タップを通常300ms以上の判定窓で抑制 | 専用のダブルタップを1回分として処理 | 解放ごとに処理するため2回分 |
| マウスのドラッグ・ホイール | 再生操作へ変換しない。一覧のホイールスクロールは可能 | Androidと同じ入力源分離ではない | TouchSurfaceのポインター解放をタップ・スワイプとして判定 |

Androidのリモコン案内は「左右＝前後ファイル、上下＝±10秒」に修正済み。一時停止時は方向キーでボタンを選ぶ案内もある。10月24日資料の修正候補は対応済みとして扱う。

根拠：[Android入力][a-main]・[ジェスチャー][a-gesture]・[スワイプ][a-swipe]・[追加タップ抑制][a-tap]・[ポインター][a-pointer]・[画面文字列][a-strings]、[iOSジェスチャー][i-player]・[外部キー][i-keys]、[Windowsキー][w-main]・[ポインター][w-player]・[カーソル][w-cursor]。

## 8. DLNA・エラー・音声出力

| 項目 | Android | iPhone／iPad | Windows |
|---|---|---|---|
| DLNA接続 | SSDP探索と、IP・ホスト名・機器情報のhttp(s) URLによる手入力 | 通常実機ビルドはアドレス指定。自動探索は権限・ビルド条件に依存 | SSDP探索。手入力画面なし |
| 保存先への再接続 | 確認済み接続先URLをサーバーID別に保存し、存在すれば探索なしで再接続 | 保存したNASアドレス等を使用 | URLは保存せず、登録先を開く際に再探索 |
| DLNAメディア | 動画・音声リソース | 参照版ではMP4／M4Vが対象 | 動画・音声リソース |
| 再生エラー | スマホ・TV、ローカル・ネットワークとも同じ項目を一時停止。自動スキップしない | 通常エラーは次へ進む。連続3件または末尾の失敗で停止。アクセスエラーは再選択案内 | 自動スキップせず、再試行／別ファイル／キャンセルを表示 |
| 再試行 | 確認済み位置を保持し、ユーザーの明示操作で同じファイルを再試行 | 再生・再読込等で復旧。通常失敗の進行規則は上記 | ダイアログの再試行から再読込 |
| 音声出力情報 | ソース形式と実際のAudioTrack・出力先を区別。対応USBの優先ミキサー設定を試行 | AVAudioSessionの出力情報。ビットパーフェクト指定機能なし | Windows標準ミキサー経由。WASAPI排他・ビットパーフェクト設定UIなし |

AndroidとWindowsは「エラーで勝手に次へ進まない」点が共通になった。iOSの自動スキップは共通仕様との差として残る。出力情報や設定成功の表示は、実際のビット一致を測定した証明ではない。

根拠：[Android DLNA接続][a-dlna]・[エラーと情報][a-main]・[現行仕様][a-spec]、[iOS DLNA][i-dlna]・[再生失敗][i-state]・[仕様][i-spec]、[WindowsDLNA・保存仕様][w-spec]・[再生処理][w-player]。

## 9. Android TV・Apple TV・Mac Catalyst

| 対象 | 現在の実装と残る差 |
|---|---|
| Android TV | スマホと同じAPK・上下各1行の操作部。SAF・USB・DLNAを利用。戻る1回でセッションを保持した一覧へ移り、キャンセルで元の状態を復元する |
| Apple TV | 専用ホームと下部操作パネル。参照版はDLNAアドレス指定のみで、SSDP・USB・標準ファイルダイアログは未対応。操作部が非表示なら戻る1回で表示し、表示中の戻るで一覧へ移る |
| TVのスワイプ方向 | Androidは右＝前・左＝次。Apple TVは左＝前・右＝次。Apple TVの左右タップ／左右ボタンは曲送りに使わない |
| Mac Catalyst | 標準タイトルバーに名前と再生操作を配置。画面下の再生操作・キー案内は出さない。Escで一覧を重ねる。別アプリへの切替だけでは止めず、背景化・音声割り込みで停止する |
| Macのマウス | 中央クリックで再生切替、左右クリックで±10秒。現在のAndroidの「場所によらず背景クリックで再生切替」とは異なる |

Android TVの戻るを「再生を終了して一覧へ移る」とする旧記載は現状に合わない。共通仕様で7環境を扱う際には、TVのスワイプ方向、戻るの段階数、Mac／Windowsの非アクティブ化時の違いを引き続き区別する。

根拠：[AndroidActivity][a-main]・[保存先][a-saf]、[Apple TV入力・画面][i-tv]・[Apple系仕様][i-spec]、[Macタイトルバー][i-mac-titlebar]・[シーン状態][i-content]。

## 10. 共通仕様に対して残る主な差と移行判断

本節は[共通操作仕様](common-operation-spec.md)を目標として見た差分であり、未対応版への実装指示や実装済み宣言ではない。

| 観点 | Android | 他版に残る主な差 |
|---|---|---|
| 上下各1行・左タイトル・右アイコン・上下端接地 | 対応 | iOSはタイトルとボタンが別行。Macはタイトルバー。Windowsは従来の上部ツールバー |
| アプリ内終了の廃止 | 対応 | Windowsはホーム・再生画面に終了ボタンが残る |
| 一覧キャンセルで元の位置・再生意図を復元 | 対応。割り込み後は停止 | iOSは一時停止へ戻る。Windowsはセッションを解放する |
| 一覧で選択したファイルを0秒から自動再生 | 再生中からの選び直しでも対応 | iOSの再選択は一時停止のまま |
| エラーで自動スキップしない | 対応 | iOSは通常失敗を次へ進める |
| ポインターの背景クリック・ダブル入力 | 位置によらず再生切替、近接した追加入力を抑制 | Mac／Windowsは左右クリックがシーク。Windowsはダブル入力が2回分 |
| TVの戻るとスワイプ方向 | 現行Androidの割当へ整理済み | Apple TVの段階的な戻ると左右スワイプ方向が異なる |
| DLNAの探索とアドレス指定 | 両方に対応 | iOSは権限条件、Apple TVは手入力のみ、Windowsは探索のみ |

10月24日資料で優先修正候補としたAndroidの案内、再選択保持、キー割当、読み込み表示、手入力導線などは対応済みである。以後の共通化は、上表のように各版の現在のコードとの差を確認して進める。

[a-layout]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/res/layout/activity_main.xml
[a-dimens]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/res/values/dimens.xml
[a-land-dimens]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/res/values-land/dimens.xml
[a-main]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/java/jp/nagu/continuousplayer/MainActivity.kt
[a-saf]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/java/jp/nagu/continuousplayer/SafFileBrowser.kt
[a-browser]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/java/jp/nagu/continuousplayer/DlnaSelectionDialog.kt
[a-item]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/java/jp/nagu/continuousplayer/VideoItem.kt
[a-gesture]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/java/jp/nagu/continuousplayer/GestureHandler.kt
[a-swipe]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/java/jp/nagu/continuousplayer/SwipeDirection.kt
[a-tap]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/java/jp/nagu/continuousplayer/TapSuppression.kt
[a-pointer]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/java/jp/nagu/continuousplayer/PointerControls.kt
[a-strings]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/res/values/strings.xml
[a-spec]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/docs/spec.md
[a-dlna]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/268d99cbc2a3de2662210229dc7c4c267dcde375/app/src/main/java/jp/nagu/continuousplayer/DlnaBrowser.kt
[i-content]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/ContentView.swift
[i-player]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/PlayerScreen.swift
[i-browser]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/MediaFileBrowser.swift
[i-browser-style]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/FileBrowserStyle.swift
[i-dlna]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/DLNABrowser.swift
[i-keys]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/PlaybackSurface.swift
[i-state]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/PlaybackState.swift
[i-tv]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_tvOS/TVPlayerScreen.swift
[i-mac-titlebar]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/CatalystPlaybackTitlebar.swift
[i-spec]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/docs/spec.md
[w-main]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.xaml.cs
[w-layout]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.xaml
[w-browser]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Browser.cs
[w-player]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Player.cs
[w-cursor]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Cursor.cs
[w-spec]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/docs/spec.md
