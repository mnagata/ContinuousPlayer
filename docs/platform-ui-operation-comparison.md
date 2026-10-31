# ContinuousPlayer：UI・操作比較（Android・Android TV・Apple 4版の現行仕様を反映）

初版調査日：2026年10月27日（日本時間）

Apple版更新日：2026年11月1日（日本時間）

Android・Android TV更新日：2026年11月1日（日本時間）

Android・Android TVを11月1日0時台のローカルHEADと同日のUIスナップショットに合わせて更新した。iOS・iPadOS・tvOS・Mac Catalystは10月31日夜の参照コミットと11月1日のUIスナップショット、Windowsは初版の参照コミットを維持する。共通操作仕様の提案、コード上の実装、画面で確認した表示、実機受入を区別する。

本書は10月27日の比較を基に、Android・Android TVとApple 4版の現行仕様を反映する更新用の比較資料である。

## 1. 調査対象と確認方法

| 版 | 参照コミット | コミット日時（日本時間） | 前回比較からの変更 |
|---|---|---|---|
| Android・Android TV | [85ff211](https://github.com/mnagata/ContinuousPlayer_for_Android/commit/85ff21122442aa6ca6ed200d11c177f6e806ef60) | 2026-11-01 00:04 | 11月1日に更新。旧参照は268d99c。ホーム文言・高精度音声出力・出力情報を更新 |
| iOS・iPadOS・tvOS・Mac Catalyst | [a4c4040](https://github.com/mnagata/ContinuousPlayer_for_iOS/commit/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5) | 2026-10-31 23:52 | 11月1日に更新。旧参照は8ec1593 |
| Windows | [6280695](https://github.com/mnagata/ContinuousPlayer_for_Win/commit/62806952a521cada0e6b018734041c3768ceba81) | 2026-10-18 16:22 | 同じ参照コミット |

画面定義、入力処理、ファイル選択、再生状態の処理を静的に照合した。文書とコードが食い違う場合はコードを優先した。特にAndroid READMEには単一SAF登録時の一覧省略など旧手順が残っているため、その記載を現行動作の根拠にはしていない。

今回のAndroid版は製品ソース・Debug Manifest・アプリのビルド設定が参照コミットと一致することを確認し、旧参照からの差分を照合した。[スマートフォン15枚][a-snapshots]と[Android TV 17枚][a-tv-snapshots]の既存記録を参照し、ホームと音声情報の画像も確認した。スマホはAndroid 16 / API 36.1、TVはAndroid 16 / API 36のエミュレーターで、同じAPK・サンプルデータを使う。TVのD-pad入力は注入され、一部フォーカスは撮影用コードで指定されている。保存済み行のUSB表記は撮影用モデルであり、実USB接続を示さない。

今回のApple版は製品ソースが参照コミットと一致することを確認し、選択・状態復元・入力・ライフサイクル・表示をコードで照合した。[11月1日の実UIスナップショット](../ui-snapshots/apple-2026-11-01.md)には、iPhone 16枚・iPad 16枚・Apple TV 12枚・Mac Catalyst 16枚を収録している。Simulator／Macのサンプル表示であり、キャンセル・復旧・物理入力の全条件を画像だけで検証したものではない。tvOSの音声情報画面は撮影ハーネスが到達できず未収録だが、表示実装は存在する。

この更新では実機入力・実NAS・USB DACの追加検証は行っていない。寸法はソースの指定値であり、dp・sp・pt・DIPの実際の見え方が同じであることを意味しない。GitHubの最新mainの継続追跡ではなく、上表の固定版同士の比較である。

## 2. 比較結果の要点

Android・Android TVは同じAPKで、再選択時のセッション保持、登録後の保存済み一覧への復帰、共通キー割当、手入力のDLNA接続、失敗時の停止に対応する。一時停止メニューは上端・下端の各1行で、アプリ内終了と重複する補助メニューは設けない。今回の更新でホームを「ANIME OPENINGS / ENDINGS」「動画フォルダーを選ぶ」に揃え、PCM形式に応じた高精度出力、対応USBのミキサー選択、出力経路・実ミキサー／HAL情報と注記を反映した。

Apple 4版も、一覧・情報の表示中にセッションを保持し、閉じると元の再生意図を復元する構成へ更新された。割り込み後は手動再開を待ち、同じファイルを含む明示選択は0秒から再生する。通常エラーの自動スキップと縦画面の常設35%情報領域は廃止され、iPhone／iPad／tvOSは上下の操作部、Macはタイトルバーと下端5操作を使う。

プラットフォーム間の違いには、直接タッチとtvOSリモコンのスワイプ方向、DLNAの探索・対応リソース、iPhoneとiPad／Macのフォーカス喪失時の扱いがある。入力種別やOS予約操作として共通案に定められた違いと、状態文の配置などの残る差を分ける。Windowsは初版参照の上部ツールバー・再選択時のプレイヤー解放・左右クリックでのシークを維持する。

## 3. ホームとアプリ終了

| 項目 | Android／Android TV | iPhone／iPad | Windows |
|---|---|---|---|
| ホーム | ロゴ、「ANIME OPENINGS / ENDINGS」、アプリ名、「動画フォルダーを選ぶ」。TVのみ操作案内 | ロゴ、英字ラベル、アプリ名、「動画フォルダーを選ぶ」、補足説明 | ロゴ、英字ラベル、アプリ名、選択、Exit、キー案内 |
| ロゴの表示領域 | 通常180dp、横画面100dp | 通常200pt、高さ450pt未満では120pt | 180DIP |
| 選択ボタン | 通常最小幅280dp・最小高さ56dp | 最大幅320pt、上下18ptの余白 | 280×56DIP |
| アプリ内終了 | ホーム・再生画面とも設けない。ホームの戻るはOSへ渡す | 設けない | ホームと再生ツールバーに終了ボタンあり |
| 共通する意匠 | 濃紺、シアン・紫、重なるフレームと再生マーク | 同じモチーフ。配色値・字体・角丸は別指定 | Android由来の配色・図形をXAMLで実装 |

Androidの「その他」→「アプリを終了」は廃止済み。Windowsに終了ボタンが残っていることは、現在の共通仕様との差である。

根拠：[Android画面][a-layout]・[寸法][a-dimens]・[横画面寸法][a-land-dimens]、[iOSホーム][i-content]、[Windows画面][w-layout]・[終了処理][w-main]。

## 4. 保存済みフォルダーとファイル選択

| 項目 | Android／Android TV | iPhone／iPad | Windows |
|---|---|---|---|
| ホームからの入口 | 保存済みが0件・1件・複数件でも必ず一覧を表示 | 保存済み一覧を表示 | 保存済み一覧を表示 |
| 登録直後 | SAF・USB・DLNAとも保存済み一覧の登録行へ戻る。登録だけでは再生しない | 端末はOSフォルダーピッカー、DLNAは階層を選んで登録。登録行を選択した保存済み一覧へ戻り、登録だけでは再生しない | 登録操作では保存済み一覧へ戻る。追加用ブラウザーから登録前に再生する経路もある |
| ブラウザー | 暗色の共通ダイアログ。最大幅880dp、高さは利用可能領域92%・最大800dp | 端末・DLNA共通のFileSelectionScreen。暗色のNavigationStack、最大880×800pt | 暗色の共通オーバーレイ。最大880×800DIP |
| ファイル行 | 20sp、縦最大2行・最小48dp、横1行・最小40dp | 標準文字サイズ、通常最大2行・低い横画面1行。行の上下に8ptの余白 | 20DIP、最大2行、最小52DIP |
| 一覧の拡張子 | 対応する末尾拡張子を省略。DLNAにも適用 | ローカル・DLNAとも対応する末尾拡張子だけ省略。途中のピリオドと未知の拡張子は保持 | ローカルは拡張子付き。DLNAはサーバーのtitleを表示 |
| 同名になる項目 | 拡張子省略後の衝突に形式バッジを付ける | ローカル・DLNA共通で省略後の衝突にMP4／M4V等の形式バッジを付ける | Androidと同じ形式バッジ処理ではない |
| 登録解除 | 保存済み行の長押し・右クリックで確認。実ファイルは削除しない | 長押し・副ボタンで確認。iPhone／iPadはスワイプ解除も持つ。実ファイル・使用中アクセスは削除しない | Delete・右クリック・長押し・解除ボタンで確認 |
| 読み込み | 既存一覧を維持し、SAF・USB・DLNAで0.5秒後に遅延表示。初回読み込みは別扱い | 旧一覧を維持し、取得開始0.5秒後に読み込み表示を重ねる。取得中の決定を拒否し、取消後の古い完了は無効化 | 既存一覧を維持。DLNAは0.5秒後に遅延表示 |

Androidでは次の補助操作を廃止している。

- 保存済み行の「その他」ボタンと登録解除用ポップアップ。登録解除は長押し・右クリックから行う。
- 「フォルダーを追加」の保存先選択画面の「保存済みフォルダー」ボタン。キャンセル・戻る・Escで一覧へ戻る。
- ファイル一覧の「名前・形式」ボタンと、ファイル行の長押し・右クリックから開くメディア情報。これらの操作で再生も始めない。詳細は再生一時停止中の「メディア情報」で確認する。

形式バッジ、元の名前・形式のツールチップと読み上げは維持する。ファイルの長押しと、保存済みフォルダーの登録解除を混同しない。

根拠：[Android保存済み・登録フロー][a-saf]・[共通ダイアログ][a-browser]・[表示名と形式][a-item]、[Apple共通一覧][i-browser]・[選択フロー][i-selection]・[表示名][i-names]・[共通スタイル][i-browser-style]、[Windows一覧処理][w-browser]・[画面][w-layout]。

## 5. 一時停止中の画面・メディア情報

| 項目 | Android／Android TV | iPhone／iPad | Windows |
|---|---|---|---|
| 上部 | 1行。拡張子を除いたタイトルが左、ホーム・ファイル一覧・メディア情報の3アイコンが右 | 1行。拡張子を省いたタイトルが左、ホーム・ファイル一覧・情報の3アイコンが右 | 高さ56DIPの1行。ホーム・名前・再開・情報・フォルダー・終了 |
| 再生コントロール | 下端中央に前・−10秒・再生／一時停止・＋10秒・次の5ボタン | 下端中央に前・−10秒・再生／一時停止・＋10秒・次の5操作。各44pt以上 | 専用の前後・シーク画面ボタンはない |
| 画面端との関係 | 上下の背景を画面端に接地。非表示のシステムバーの高さを外側に予約しない | 映像に上下の操作部を重ね、背景は画面端まで延ばす。切り欠き等の余白は内側に置く | 上部ツールバーと下部情報・状態文として配置 |
| 長いタイトル | 左側の残り幅で1行・末尾省略。アイコンを押し出さない | 既知の末尾拡張子を省き、残り幅で1行・末尾省略。元名は読み上げ・ツールチップ・情報に保持 | ツールバー内で省略 |
| 縦画面 | 操作部の表示中は重複する補助情報を隠す。操作部のために映像領域を変えない | 常設35%情報領域を廃止。縦横とも映像全体を使い、操作部の表示で映像領域を変えない | 縦長ウィンドウでは映像上に下部情報を重ねる |
| 音声のみ | タイトル・操作部を常時表示 | 取得トラックで音声のみと判定。中央の波形とタイトル・操作部を常時表示 | 中央に音声アイコンとファイル名 |
| 終了・エラー | 操作部を表示し、上端の同じ行内に状態文を表示 | 終了は「このフォルダーの再生が終了しました。」をタイトル下に表示。エラーは停止して復旧パネルを表示 | 状態文を表示。エラーはダイアログで案内 |
| 詳細情報 | ファイル→映像・音声形式→音声出力の順。元の名前、出力経路、AudioTrack設定、診断可能な場合の実ミキサー／HAL、出力の注記を表示 | ファイル→映像・音声形式→音声出力の情報シート。開く間は一時停止し、閉じると元の意図を復元 | スクロール可能な再生情報ダイアログ |

Androidでは、表示中のシステムバーや切り欠きを避ける余白をメニューの内側に加える。背景は上端・下端に接したまま、下段の高さを必要に応じて広げる。各ボタンは48dp。上下のフォーカス移動とTab順も、上部3アイコン・下部5ボタンに対応する。

iPhone／iPadも上下の操作部へ更新済みである。終了・中断等の状態文はタイトル左領域内の下段に表示するため、共通案の「上端の同じ行内」とは差がある。Windowsの「上部だけの操作部」は別の配置である。

根拠：[Android画面][a-layout]・[Insets・状態更新][a-main]・[現行仕様][a-spec]、[iOS／iPadOS再生画面][i-player]・[情報シート][i-info]、[Windows画面][w-layout]・[再生処理][w-player]。

## 6. 選び直し・キャンセル・割り込み

| 場面 | Android／Android TV | iPhone／iPad | Windows |
|---|---|---|---|
| 再生中に一覧を開く | 同じプレイヤー・キュー・位置・再生意図を保持して一時停止 | 同じプレイヤー・キュー・位置・意図を保持して一時停止し、映像上に独立した一覧を重ねる | プレイヤーを解放し、再生元の一覧へ移る |
| 一覧をキャンセル | 元の位置と再生／一時停止状態へ戻る。割り込み後やアクセス失効時は自動再開しない | 元のファイル・位置・キュー・再生／一時停止状態へ戻る。表示中の割り込み・アクセス失効・失敗があれば自動再開しない | 元の位置へ戻るセッション保持経路はない |
| 再生から開いた一覧でファイルを確定 | 同じファイルでも0秒から新しいセッションで自動再生 | 同一・別ファイルとも新セッションの0秒から自動再生。一時停止中の選択も同じ。確定後の割り込み等では停止を優先 | 選択先を0秒から再生 |
| 再生画面の戻る／Esc | スマホ・TVの戻るとEsc／Oは、現在ファイルを指定して一覧を開く | iPhone／iPadも映像操作対象時にEsc／Oで一覧を開く。情報等が最前面ならそちらを優先 | Esc／Oで再生を停止して再生元一覧へ |
| ホーム操作 | 再生セッションを終了してホーム | 再生を終了してホーム | 再生を終了してホーム |
| バックグラウンド・ロックから復帰 | 元のファイル・位置で一時停止し、明示再開を待つ | 同じファイル・確認済み位置で一時停止。復帰・権限回復だけでは再開せず、明示再開を待つ | 非アクティブ化で一時停止し、自動再開しない |
| 通常のウィンドウフォーカス喪失 | それだけでは再生を停止しない | iPhoneは非アクティブ化で停止。iPadは画面に見えているforegroundInactiveだけでは止めず、背景化・ロックでは停止 | ウィンドウ非アクティブ化で停止 |
| 再起動後 | 登録先・最後の再生元フォルダーを利用。秒数・キューは永続復元しない | 登録先等を保持。秒数は永続復元しない | 登録先・再生元・ファイルIDを保存。秒数・キューは復元しない |

Apple 4版でも「一覧キャンセルによる意図の復元」と「割り込み後の手動再開待ち」を分離している。情報シートも同じ一時表示スナップショットを使い、表示中のロック等があれば閉じても停止を維持する。

一覧内の親移動はBackspace／TVの戻るで1階層、登録ルートから保存済みへ、保存済みの最上位から全体取消となる。追加元・サーバー・登録中の子画面のキャンセルは保存済みへ戻る。再生ファイル一覧と保存済みのキャンセルは選択全体を閉じる。Macの全画面中のEscはOSの全画面解除を優先する。

根拠：[Android保持・復元・入力処理][a-main]、[Apple共通選択][i-scene-model]・[一時表示と再生制御][i-controller]・[ライフサイクル][i-lifecycle]、[Windows一覧復帰][w-player]・[仕様][w-spec]。

## 7. 入力機器別の比較

再生領域が操作対象の場合の割当を示す。AndroidとApple版は、フォーカス中のボタン・一覧・文字入力を優先する。Android欄はAndroid TVにも共通である。Mac CatalystのOS全画面キーは10節の例外を参照。

| 操作 | Android／Android TV | iPhone／iPad | Windows |
|---|---|---|---|
| タッチの左／中央／右1/3 | −10秒／再生切替／＋10秒 | 同じ。映像領域が対象 | 同じ |
| マウスの背景クリック | 場所によらず再生切替。近い位置のダブルクリックは1回分 | 対応ポインターの主ボタンは位置によらず再生切替。追加入力・ドラッグ・ホイールを再生操作にしない | 左／右でシーク、中央で再生切替。解放ごとに処理 |
| ←／→ | 前／次 | 前／次 | 前／次 |
| ↑／↓ | −10秒／＋10秒 | −10秒／＋10秒 | −10秒／＋10秒 |
| 再生切替キー | Space・Enter・TV決定・メディアキー | Space・Enter・メディアキー。映像コマンドの長押しは重複実行しない | Space・Enter・メディアキー |
| Shift＋←／→ | −10秒／＋10秒 | −10秒／＋10秒 | 専用の修飾キー割当なし |
| 情報 | I・INFO | I | I |
| 一覧・ホーム | Esc／Oで一覧、Hでホーム | Esc／Oで一覧、Hでホーム。情報・一覧・入力欄の所有するキーを優先 | Esc／Oで一覧、Hでホーム |
| 一時停止ボタン間の移動 | 方向キー・Tab／Shift＋Tab | Tab／Shift＋Tab等で部品へ移動。フォーカス部品・文字入力・最前面画面を映像コマンドより優先 | Tab／Shift＋Tab。矢印は前後・シークを維持 |
| 動画のポインター無操作 | 3秒で操作部とカーソルを隠す。ボタン上・フォーカス中等は維持 | 移動で操作部を表示し、再生中3秒無操作で操作部とカーソルを隠す。ホバー・焦点・モーダル・VoiceOver中は維持 | 3秒でカーソルを隠す。操作部は一時停止状態で表示 |

### タップ・スワイプの判定

| 項目 | Android／Android TV | iPhone／iPad | Windows |
|---|---|---|---|
| 右／左スワイプ | 前／次 | 前／次 | 前／次 |
| 横スワイプの成立条件 | 横70dp以上かつ横が縦の1.5倍以上。境界を含み、速度条件なし | 横70pt以上かつ横が縦の1.5倍以上。境界を含み、速度条件なし | 横100DIP超、横が縦より大きく、横速度100DIP/秒超 |
| ダブルタップ | 初回の指離しで実行し、近接する同領域の追加タップを通常300ms以上の判定窓で抑制 | 初回の指離しで実行。同領域・32pt以内・0.3秒以内の追加タップを抑制して1回分 | 解放ごとに処理するため2回分 |
| マウスのドラッグ・ホイール | 再生操作へ変換しない。一覧のホイールスクロールは可能 | 直接タッチと間接ポインターを分離。ポインタードラッグ・ホイールは再生操作にせず、一覧はスクロール可能 | TouchSurfaceのポインター解放をタップ・スワイプとして判定 |

Androidのリモコン案内は「左右＝前後ファイル、上下＝±10秒」に修正済み。一時停止時は方向キーでボタンを選ぶ案内もある。10月24日資料の修正候補は対応済みとして扱う。

根拠：[Android入力][a-main]・[ジェスチャー][a-gesture]・[スワイプ][a-swipe]・[追加タップ抑制][a-tap]・[ポインター][a-pointer]・[画面文字列][a-strings]、[Apple入力判定][i-input]・[映像入力面][i-keys]・[メディアキー][i-media-keys]、[Windowsキー][w-main]・[ポインター][w-player]・[カーソル][w-cursor]。

## 8. DLNA・エラー・音声出力

| 項目 | Android／Android TV | iPhone／iPad | Windows |
|---|---|---|---|
| DLNA接続 | SSDP探索と、IP・ホスト名・機器情報のhttp(s) URLによる手入力 | 通常実機ビルドはアドレス指定。自動探索は権限・ビルド条件に依存 | SSDP探索。手入力画面なし |
| 保存先への再接続 | 確認済み接続先URLをサーバーID別に保存し、存在すれば探索なしで再接続 | 登録先の機器記述URLを直接取得しサーバーIDを照合。失敗時は探索が利用可能な環境だけ再探索 | URLは保存せず、登録先を開く際に再探索 |
| DLNAメディア | 動画・音声リソース | MP4／M4V系リソースが対象。音声のみのMP4も再生できる | 動画・音声リソース |
| 再生エラー | スマホ・TV、ローカル・ネットワークとも同じ項目を一時停止。自動スキップせず、再試行／次へ（次がある場合）／ファイル一覧／閉じるを表示 | 失敗した同じ項目・キュー・確認済み位置を保持して停止。自動スキップしない。権限失効は再登録を案内 | 自動スキップせず、再試行／別ファイル／キャンセルを表示 |
| 再試行 | 確認済み位置を保持し、ユーザーの明示操作で同じファイルを再試行 | 明示的な再試行で同じファイルの確認済み位置へ。次がある場合だけ「次のファイル」も表示 | ダイアログの再試行から再読込 |
| 音声出力情報 | ソース形式・AudioTrack設定・確認済み出力先・優先ミキサーを区別。Debug診断時は実ミキサー／HALも表示 | AVAudioSessionの出力情報。ビットパーフェクト指定機能なし | Windows標準ミキサー経由。WASAPI排他・ビットパーフェクト設定UIなし |

Apple 4版も「エラーで勝手に次へ進まない」に対応済みである。失敗パネルは再試行・次のファイル（次がある場合のみ）・ファイル一覧・閉じるを持ち、閉じるだけでは復旧・再開しない。再生が2秒進まないとバッファリング、30秒進まないと同じ項目で失敗扱いにする。

シークは実際のseekable rangeを使う。既知長の末尾以上へ進める場合は次があれば次の0秒、次がなければ位置を変えない。不明長・シーク不可から長さを推測しない。再生／停止意図は前後移動・シークで保持する。音声出力情報は4版ともAVAudioSessionの現在値で、ビット一致を測定した証明ではない。

根拠：[Android DLNA接続][a-dlna]・[エラーと情報][a-main]・[現行仕様][a-spec]、[Apple DLNA接続][i-dlna]・[DLNAリソース判定][i-dlna-client]・[探索条件][i-discovery]・[再生状態][i-state]・[失敗・復旧制御][i-controller]・[失敗パネル][i-failure]・[出力情報][i-info]、[WindowsDLNA・保存仕様][w-spec]・[再生処理][w-player]。

### Android・Android TVの音声出力と情報表示

出力形式とUSB設定は、再生開始ファイルの情報だけで固定せず、各AudioTrackへ渡すPCM形式に合わせて更新する。追加の設定画面ではなく自動選択で、スマホ・TVとも同じ処理を使う。

| 条件 | 現行動作 |
|---|---|
| PCM形式 | 24bit・32bit整数PCMとFloat PCMは32bit Float出力、16bit整数PCMは16bit出力。圧縮音源は対応デコーダーにFloatを要求し、返されたPCM形式に従う |
| USBがFloat非対応 | 同じレート・チャンネルマスクの整数32bit PCMに対応する場合、Floatデータを整数32bitへ変換して出力する |
| USB優先ミキサー | OSが選んだ単一USB出力に対し、実際のPCM形式・レート・チャンネルマスクが一致するBIT_PERFECT、次にDEFAULTを試す。候補なし・取得失敗ではOS標準出力を使う |
| 高精度出力の初期化失敗 | 適用済み優先設定があれば解除して再試行し、それでも失敗すれば同じレート・チャンネル数のPCM16へ変換して再試行。PCM16も失敗すると同じファイルで停止する |
| 曲・経路・機器の変更 | 新しいPCM形式や出力先へ設定を再評価し、古い設定を解除する。使用中の出力切断は一時停止し、再接続だけでは再開しない |
| 情報画面 | 確認した出力経路・出力デバイス、AudioTrack形式・レート・チャンネル数、Bit-Perfect設定・優先ミキサーを表示。開く直前の経路情報を保持し、画面表示中は一時停止する |
| 実ミキサー／HAL | ADBでDUMP権限を付与したDebug版だけ、再生中に診断する。自アプリのセッション・出力先を照合できた取得結果から、実ミキサー名・種類・I/O番号、ミキサー形式、HAL形式を表示する |

通常の経路表記は「AudioTrack → Android → 出力先」、診断に成功した場合は「AudioTrack → 実ミキサー名 → HAL → 出力先」となる。情報画面では直前に取得済みの結果を使い、停止中の追加診断はしない。Debug版で診断未取得・権限未付与の場合、実ミキサー／HAL欄は「取得できません」と表示し、通常版ではこれらの診断欄を表示しない。確認時刻・診断状態の行は設けない。

音声出力欄には、AudioTrack形式・レートはアプリ設定値、ミキサー／HALは診断できた場合のみ表示、DAC実出力は未測定、という注記を表示する。音声トラックなしの場合は「音声出力：なし」とし、注記は隠す。11月1日のスマホ・TVの画像では内蔵スピーカー、PCM 16-bit / 48000 Hz / 1 ch、実ミキサー／HALの取得不可を確認できる。実USB DACやビット一致の測定結果としては扱わない。OSの出力先をUSBへ強制せず、最大レートへのアップサンプリングも行わない。

32bit Floatは24bit整数PCMの精度を保持できるが、32bit整数PCMの全ビット一致を保証しない。「Bit-Perfect設定：設定済み」は、確認した出力先・AudioTrack形式と適用済みの優先属性が一致することを示し、元ファイルからDACまでの全ビット一致を示すものではない。

根拠：[再生エンジン][a-controller]・[USB PCM出力][a-usb-audio]・[出力方針][a-output-policy]・[ミキサー設定と情報][a-audio]・[出力監視][a-audio-monitor]・[Debug診断][a-diagnostics]・[情報項目][a-info]・[現行仕様][a-spec]、[スマホの収録記録][a-snapshots]・[TVの収録記録][a-tv-snapshots]。

## 9. Android・Android TVの個別仕様

Android欄の一覧・状態保持・入力・音声出力は両版に共通する。違いは主にTVモードの入口とフォーカスであり、TV専用Activityや別APKは使わない。最低OSは両方ともAndroid 14 / API 34である。

| 観点 | スマートフォン・タブレット | Android TV |
|---|---|---|
| ホーム | 英字見出し・ロゴ・アプリ名・選択ボタン。縦横の寸法に適応 | 同じ構成にリモコン案内を追加し、選択ボタンへ初期フォーカス |
| フォルダー追加 | SAF・DLNA | SAF・USB・DLNA。SAFを起動できなければ案内し、USBを明示選択して進む |
| USBアクセス | OSのSAF選択を使う | アプリ内USBブラウザーには「すべてのファイルへのアクセス」が必要。案内からOS設定を開き、復帰時に許可を再確認。ブラウザーは読み取り専用 |
| USBブラウザー | TV専用の追加入口は表示しない | マウント済みの取り外し可能ストレージを列挙し、最後に成功した閲覧位置を記憶。登録すると保存済み一覧の登録行へ戻る。未接続・読取失敗でも登録を保持 |
| 再生中の方向操作 | タッチは左／中央／右で−10秒／切替／＋10秒、右／左スワイプで前／次。接続キー・ポインターにも対応 | 映像にフォーカスがあれば左右＝前後、上下＝±10秒、決定＝再生切替。一時停止時は再生ボタンへ、再開時は映像へフォーカス |
| ファイル一覧 | タップ・クリックで選択、ホイールでスクロール | 上下で行、決定で開く／再生。一覧にフォーカスがあると左右・Page Up／Downでページ移動。保存済み・サーバー・追加先一覧にはページ操作を適用しない |
| 登録解除 | 保存済み行の長押し・右クリックで確認 | 保存済み行の決定キー長押し・右クリックで確認。初期フォーカスはキャンセル |
| 情報画面 | スクロール可能なダイアログ。OK・I・Esc・戻るで閉じる | 同じ情報を表示し、OKへ初期フォーカス。横長画面では下の出力情報・注記までスクロールする |

再生画面の戻る1回はスマホ・TVとも、セッションを保持したまま現在ファイルを指定して一覧を開く。コードには対象行の選択・表示処理があるが、11月1日のTV撮影用セッションでは開いた直後に子フォルダー行が選ばれ、下キーで動画行へ移っている。この画像だけで現在ファイルへのフォーカス復元が合格したとは扱わない。

11月1日のTV記録には保存済み0件・1件、USB入口と許可案内、再生ボタンから上キーで情報へ移る状態、再生再開後のplayer_viewへのフォーカス、音声・終了・エラーを収録している。実TV・4K・文字拡大、物理リモコンの長押し、実USBの許可取得・ドライブ閲覧、実NAS、シーク不可・中断復帰の受入は別途必要である。

根拠：[画面・TV判定と入力][a-main]・[保存済みと追加先][a-saf]・[USB追加ブラウザー][a-usb-browser]・[登録USBの選択][a-usb-playback]・[一覧キーとフォーカス][a-browser]・[Manifest][a-manifest]、[スマホ記録][a-snapshots]・[TV記録][a-tv-snapshots]。

## 10. iOS・iPadOS・tvOS・Mac Catalystの個別仕様

選択・キャンセル・エラーの状態処理は4版共通のPlayerSceneModel／PlaybackControllerが所有する。ホームや選択画面が最前面の間は背後のプレイヤー入力を無効にし、ホーム・ウィンドウ切断では保持セッションを終了する。

| 観点 | iOS / iPhone | iPadOS / iPad | tvOS / Apple TV | Mac Catalyst |
|---|---|---|---|---|
| ホーム | ロゴ・英字ラベル・動画フォルダーを選ぶ | iPhoneと同じ構成で画面サイズに適応 | 専用ホーム、NAS説明、リモコンの選択フォーカス | 共通ホームと標準ウィンドウ |
| 保存元 | 端末／外部ストレージ、DLNA | 端末／外部ストレージ、DLNA | DLNAアドレス指定のみ。SSDP・USB・OSフォルダー選択は持たない | OSフォルダー選択、DLNAアドレス指定。探索可否はビルド設定に依存 |
| ファイル選択 | 最大880×800ptの共通一覧 | 同じ共通一覧。大画面で中央に配置 | 最大1320×900ptの暗色共通一覧。上下で行、左右でページ、下端からフッターへ | 共通一覧、現在行の強調とネイティブセルによるスクロール量の算出 |
| 再生操作部 | 上端3操作、下端5操作。縦横とも映像に重ねる | 同じ。常設35%情報領域は廃止 | 上端3操作・下端5操作。停止後は再生ボタン、再開後は映像へフォーカスを戻す | 通常はタイトルバーに名前・ホーム・一覧・情報・全画面、下端に5操作。全画面は画面内上端へ切替え、重複を避ける |
| 情報・失敗 | 情報シート、失敗パネル | 情報シート、失敗パネル | 情報と失敗をシートで表示。情報の下部に閉じるボタン | 情報はネイティブシート、失敗は画面内パネル |
| フォーカス喪失 | 非アクティブ化で一時停止 | 表示中のforegroundInactiveだけなら継続。背景化・ロックで停止 | アプリ非アクティブ化で停止 | 表示中のキーウィンドウ喪失や全画面Space切替だけでは継続。最小化・アプリ非表示・画面／システムスリープ・ロック等で停止 |
| 終了と再起動 | 登録は保持、再生セッションは永続復元しない | 同じ | 同じ | 同じ。再生開始で勝手に全画面化せず、対象ウィンドウの操作で切り替える |

### TVリモコンと直接タッチの差

Android TVは11月1日の現行参照でも同じAPK・SAF／USB／DLNA・上下操作部を使う。Apple TVも、再生画面の戻る1回でセッションを保持したファイル一覧を開く。情報・失敗シート・選択画面が最前面なら、戻るはその画面が先に処理する。旧版の「1回目で操作部を表示、2回目で一覧」という段階は廃止済みである。

現行Apple TVの映像入力面は、左／右方向ボタンで前／次、上／下で−10秒／＋10秒、決定で再生切替を行う。同じ押下の繰り返しと、押下を伴った間接タッチの終了スワイプによる重複を抑止する。

**tvOSの間接タッチの横スワイプは左＝前・右＝次**で、[共通案5.5節](common-operation-spec.md#55-tvosリモコンのタッチ面)の方向入力に対応する。iPhone／iPadとAndroidの直接タッチは右＝前・左＝次であり、入力種別として意図的に異なる。tvOSは上下スワイプも±10秒へ変換する。操作部にフォーカスがある間の方向入力はボタン移動に使用する。物理Siri Remoteの世代別動作を今回の画像から保証しない。

### Macのポインター・全画面・入力所有

映像背景の主ボタンクリックは左右位置によらず再生切替とし、近接ダブルクリックは1回分、ドラッグとホイールは再生コマンドへ変換しない。非アクティブなウィンドウを前面にする最初の映像クリックも再生コマンドにしない。iPadの対応ポインターも直接タッチと区別する。

Macの全画面は標準の切替ボタンで対象ウィンドウだけを切り替える。**全画面中のEscはOSの全画面解除が優先**され、通常ウィンドウでは再生面から一覧、一覧から取消へ使う。[共通案7.2節](common-operation-spec.md#72-全画面)のOS優先規則に対応し、同じEscで一覧も開かない。Oでは一覧を開ける。情報はIまたはEscで閉じるが、全画面中のEscはこのOS優先規則に従う。

ポインターは移動で操作部を表示し、再生中3秒無操作で操作部・カーソルを隠す。操作部のフォーカス、ボタンのホバー、モーダル表示、VoiceOver、音声のみでは表示を維持する。Tabで映像・ネイティブタイトルバー・下部操作の間を移動し、文字／IME・最前面画面・フォーカス部品を映像キーより優先する。

iPhone／iPad／Macの一覧は上下で行、左右・Page Up／Downで可視行数分のページ移動、Returnで確定、Backspaceで親移動を行い、上下端を循環しない。現在の可視セルからページ件数を計算する。Macの一覧の選択行は、見える位置にあればそのまま、外れたときだけ必要量をスクロールする。

根拠：[共通シーン][i-scene]・[セッションと選択][i-scene-model]・[一覧][i-browser]・[一覧状態][i-selection]・[入力判定][i-input]・[tvOS画面と入力][i-tv]・[Macタイトルバー][i-mac-titlebar]・[ライフサイクル][i-lifecycle]・[Macネイティブ処理][i-mac-native]・[操作部の表示制御][i-chrome]・[情報のキー所有][i-info-keys]。

## 11. 共通仕様への対応・残る差と移行判断

本節は[共通操作仕様](common-operation-spec.md)を目標として見た差分であり、未対応版への実装指示や全環境の受入合格宣言ではない。Apple版の対応を理由に、共通仕様書の「提案」状態を変更しない。

| 観点 | Android・Android TV（11月1日更新） | Apple 4版（11月1日更新） | Windows（初版参照） |
|---|---|---|---|
| 上下の操作部と映像位置 | 対応 | iPhone／iPad／tvOSは上下、Macはタイトルバー＋下部。iPhone／iPad／tvOSの状態文は左タイトル下の補助行で、共通案の「同じ行内」には差がある。終了文も共通案6節の「最後まで再生しました」と異なる | 従来の上部ツールバー |
| アプリ内終了 | 廃止済み | プレイヤー／ホームに終了操作を追加しない。MacのOS標準終了は別 | ホーム・再生に終了ボタン |
| 一覧取消で位置・再生意図を復元 | 対応。割り込み後は停止 | 4版共通で対応。情報を閉じる場合も同じ。割り込み・失敗時は停止 | セッションを解放する |
| 明示選択で0秒から自動再生 | 対応 | 同じファイル・一時停止中も対応。旧autoplay=false固定は廃止 | 対応 |
| エラーの自動スキップ禁止 | 対応 | 対応。確認済み位置で停止し、明示再試行を待つ | 対応 |
| ポインター背景クリック・ダブル入力 | 位置によらず切替、追加を抑制 | 位置によらず切替、追加を抑制。非アクティブクリック・ドラッグ等を分離 | 左右位置でシーク、ダブル入力は2回分 |
| TVの戻る・方向・スワイプ | 方向ボタンは共通割当 | 戻る1回と方向ボタンは更新済み。間接スワイプも左＝前・右＝次で共通案5.5節に対応。直接タッチとの違いを維持 | TV対象外 |
| DLNAの探索とアドレス指定 | 両方 | 通常iOS／iPadOS・Macの探索はビルド条件、tvOSはアドレス指定のみ。リソースはMP4／M4V系に限定 | 探索のみ |
| 音声出力と診断 | PCMに応じた高精度出力・対応USBの優先設定。AudioTrack設定と診断時の実ミキサー／HALを区別し、DAC未測定の注記を表示 | AVAudioSessionの出力情報。ビットパーフェクト指定はない | OS標準ミキサー経由 |
| 表示中フォーカス喪失と背景化 | 単なるフォーカス喪失では停止しない | iPad／Macは表示中の焦点移動と背景化等を区別。iPhone／tvOSは非アクティブ化で停止 | 非アクティブ化で停止 |
| 全画面のEsc | 一覧 | Macは共通案7.2節のOS優先規則に対応。全画面解除と通常ウィンドウの一覧操作を分離 | 再生停止・一覧へ |

10月24日資料で優先修正候補としたAndroidの案内、再選択保持、キー割当、読み込み表示、手入力導線などは初版の時点で対応済みだった。今回のApple版更新により、旧記載の「取消後は停止固定」「再選択後も停止」「通常エラーの自動スキップ」「縦画面35%情報欄」「Macの左右クリックシーク」は現行仕様から除外した。

Android・Android TVの今回の変更はホーム文言と音声出力・情報表示が中心であり、初版のセッション保持・キー割当を撤回する変更はない。現在ファイルへのフォーカス復元は、コード上の対応と撮影時の選択位置を区別して継続確認する。

表示の確認範囲は[Android][a-snapshots]・[Android TV][a-tv-snapshots]・[Apple 4版スナップショット](../ui-snapshots/apple-2026-11-01.md)と各manifestの未収録欄を参照する。現行コードに実装があることと、物理入力・実NAS・USB・全OS構成で検証済みであることを分けて、残る差を判断する。

[a-layout]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/res/layout/activity_main.xml
[a-dimens]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/res/values/dimens.xml
[a-land-dimens]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/res/values-land/dimens.xml
[a-main]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/MainActivity.kt
[a-saf]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/SafFileBrowser.kt
[a-browser]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/DlnaSelectionDialog.kt
[a-item]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/VideoItem.kt
[a-gesture]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/GestureHandler.kt
[a-swipe]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/SwipeDirection.kt
[a-tap]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/TapSuppression.kt
[a-pointer]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/PointerControls.kt
[a-strings]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/res/values/strings.xml
[a-spec]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/docs/spec.md
[a-dlna]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/DlnaBrowser.kt
[a-controller]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/PlayerController.kt
[a-usb-audio]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/UsbPcmAudioOutputProvider.kt
[a-output-policy]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/AudioOutputPolicy.kt
[a-audio]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/BitPerfectAudioManager.kt
[a-audio-monitor]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/PlaybackAudioMonitor.kt
[a-diagnostics]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/AudioMixerDiagnostics.kt
[a-info]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/MediaInformation.kt
[a-usb-browser]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/UsbFileBrowser.kt
[a-usb-playback]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/java/jp/nagu/continuousplayer/UsbPlaybackBrowser.kt
[a-manifest]: https://github.com/mnagata/ContinuousPlayer_for_Android/blob/85ff21122442aa6ca6ed200d11c177f6e806ef60/app/src/main/AndroidManifest.xml
[a-snapshots]: ../ui-snapshots/android/2026-11-01/README.md
[a-tv-snapshots]: ../ui-snapshots/android-tv/2026-11-01/README.md
[i-content]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/ContentView.swift
[i-player]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/PlayerScreen.swift
[i-browser]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/FileSelectionScreen.swift
[i-browser-style]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/FileBrowserStyle.swift
[i-dlna]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/DLNAServersView.swift
[i-keys]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/PlaybackSurface.swift
[i-state]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/PlaybackState.swift
[i-tv]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_tvOS/TVPlayerScreen.swift
[i-mac-titlebar]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/CatalystPlaybackTitlebar.swift
[w-main]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.xaml.cs
[w-layout]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.xaml
[w-browser]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Browser.cs
[w-player]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Player.cs
[w-cursor]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Cursor.cs
[w-spec]: https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/docs/spec.md

[i-scene]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/PlayerScene.swift
[i-scene-model]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/PlayerSceneModel.swift
[i-selection]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/FileSelectionCoordinator.swift
[i-names]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/MediaDisplayName.swift
[i-controller]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/PlaybackController.swift
[i-input]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/PlaybackInput.swift
[i-media-keys]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/PlaybackMediaCommands.swift
[i-info]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/MediaInfoSheet.swift
[i-failure]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/PlaybackFailurePanel.swift
[i-lifecycle]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/PlaybackSceneLifecycle.swift
[i-mac-native]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/MacSupport/PlaybackLifecycle.m
[i-chrome]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/PlaybackChromeState.swift
[i-dlna-client]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/DLNAClient.swift
[i-discovery]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/DLNADiscovery.swift
[i-info-keys]: https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5/ContinuousPlayer_for_iOS/InformationKeyboardHost.swift
