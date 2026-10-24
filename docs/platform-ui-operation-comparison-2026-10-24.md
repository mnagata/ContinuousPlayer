# ContinuousPlayer：Android・iOS・WindowsのUI／デザイン／操作比較

調査日：2026年10月24日（日本時間）

## 調査対象・方法

GitHubの各リポジトリのmainを対象に、README・仕様書と現在の画面定義・操作処理を照合した。ローカルのAndroid作業ツリーにはGitHubと異なるファイルがあるため、比較にはGitHub上の固定版を使用した。

| 版 | 調査対象コミット | 最終コミット日時（日本時間） |
|---|---|---|
| Android | [0267a99](https://github.com/mnagata/ContinuousPlayer_for_Android/commit/0267a9902e08c5830886a0ee74025020e65d9e4a) | 2026-10-23 23:07 |
| iOS | [8ec1593](https://github.com/mnagata/ContinuousPlayer_for_iOS/commit/8ec1593e2cd5724158834c6d305c3b11cdd8906d) | 2026-10-23 23:13 |
| Windows | [6280695](https://github.com/mnagata/ContinuousPlayer_for_Win/commit/62806952a521cada0e6b018734041c3768ceba81) | 2026-10-18 16:22 |

主比較はAndroidのスマートフォン／タブレット、iOSのiPhone／iPad、Windows。Android TV、同じiOSリポジトリに含まれるMac CatalystとApple TVも別途確認した。

今回はソースコードの静的調査。3版を起動して並べる実画面比較、実機入力、NAS・USB・音声出力の検証は実施していない。寸法は実装上の指定値で、dp・sp・pt・DIPの実際の見え方はOSや表示倍率に依存する。

## 結果

**差はある。ブランドと基本的な再生ジェスチャーは共通だが、画面の細部と選択・キャンセル・エラー時の操作は揃っていない。特にAndroidとWindowsが近く、iOSはOS標準UIと独自の再選択フローを持つ。**

WindowsのREADMEもAndroidの仕様・コード・デザイン資産をもとにした移植と明記している。[Windows README](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/README.md)

## 1. ホーム画面のデザイン

| 項目 | Android | iPhone／iPad | Windows |
|---|---|---|---|
| 基本構成 | ロゴ、英字ラベル、アプリ名、選択、Exit | ロゴ、英字ラベル、アプリ名、選択、説明文 | Androidに近い構成＋キー操作案内 |
| ブランド | 重なるフレーム・再生マーク、濃紺・シアン・紫 | 同じモチーフを画像として使用 | 同じモチーフをXAMLのパスで再現 |
| 背景色 | #123652 → #101743 → #35204F | RGB比率指定。概ね #0F1742 → #142E47 → #29174D | Androidと同じ3色 |
| ロゴの表示領域 | 通常180dp、横向き100dp | 通常200pt、高さ450pt未満では120pt | 180DIP |
| 選択ボタン | 280×56dp、丸み28dp | 最大幅320pt、上下18ptの余白、丸み18pt | 280×56DIP、丸み28DIP |
| アプリ名の文字 | sans-serif-medium、最大32sp、自動縮小 | 32pt、太字、rounded | 32DIP、SemiBold、縮小対応 |
| アプリ終了 | Exitあり | Exitなし | Exitあり |
| 説明・案内 | TVだけリモコン案内 | 登録済みフォルダーの説明文 | キーボード案内を表示 |

同じ配色の方向性でも、iOSのグラデーション色・ボタン幅・角丸・字体・補足文は異なる。ピクセル単位で同じデザインではない。

根拠：[Androidホーム定義](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/res/layout/activity_main.xml)、[Android背景](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/res/drawable/home_background.xml)、[iOSホーム定義](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/ContentView.swift#L340)、[iOS配色](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/HomeAppearance.swift)、[Windows画面](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.xaml)、[Windows配色](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/Styles/Brand.xaml)

## 2. フォルダー・ファイル選択画面

| 項目 | Android | iPhone／iPad | Windows |
|---|---|---|---|
| アプリ内ブラウザー | 暗色の共通ダイアログ | 暗色のSwiftUI一覧・NavigationStack | 暗色の共通オーバーレイ |
| サイズ上限 | 幅880dp、高さ800dpか利用可能領域92% | 最大880×800pt、周囲12ptの余白 | 最大880×800DIP、周囲12／24DIP |
| 上部操作 | 戻る・キャンセル・名前。DLNAでは再読込 | 標準ナビゲーション、キャンセル、名前。DLNAでは再読込も配置 | 戻る・キャンセル・名前・再読込 |
| ファイル名の文字・行数 | 20sp。縦は最大2行、横は1行 | 標準文字サイズ、最大3行 | 20DIP、最大2行 |
| ファイル行の高さ | 縦最小48dp、横最小40dp | 通常最小44pt、iPhone横向き40pt | 最小52DIP |
| メディアの拡張子 | ファイル一覧では非表示 | ローカルは非表示。DLNAはサーバーのtitleをそのまま表示 | ローカルは拡張子付き。DLNAはサーバーのtitleをそのまま表示 |
| 再生アイコン | 丸枠の再生マーク | play.circle | Windowsの再生グリフ |
| 長い名前 | 横向きは1行省略 | 3行まで折返し | 2行まで折返し |
| 保存済み登録解除 | 長押し→解除 | 長押し、または左スワイプ→解除 | Delete、右クリック、長押し、解除ボタン |
| 読み込み中 | 既存一覧を維持。DLNAは0.5秒後にスピナー | 対象のローカル／DLNA一覧を読み込み表示に切替 | 既存一覧を維持。DLNAは0.5秒後にスピナー |

拡張子についてはiOSも完全には統一されていない。ローカルはdeletingPathExtensionを使うが、DLNAはentry.titleを表示する。NASが拡張子付きタイトルを公開すれば表示される。

根拠：[Android共通ブラウザー](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/java/jp/nagu/continuousplayer/DlnaSelectionDialog.kt)、[iOSローカル一覧](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/MediaFileBrowser.swift)、[iOSブラウザー共通スタイル](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/FileBrowserStyle.swift)、[iOS DLNA一覧](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/DLNABrowser.swift#L603)、[Windows一覧生成](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Browser.cs#L44)、[Windows画面](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.xaml)

## 3. 再生画面・情報表示

| 項目 | Android | iPhone／iPad | Windows |
|---|---|---|---|
| 通常再生 | 黒背景、標準再生コントローラーを隠す | 黒背景、独自ジェスチャー | 黒背景、標準再生コントローラーを隠す |
| 一時停止時の上部 | 1段・高さ48dp。ホーム、名前、再開、情報、フォルダー、終了 | 名前とボタンを2段に配置。ホーム、フォルダー、情報、再生 | 1段・高さ56DIP。Androidと同じ機能の5ボタン |
| 下部の操作ボタン | 専用の前後／シークボタンなし | 前、−10秒、再生、＋10秒、次のボタンあり | 専用の前後／シークボタンなし |
| 縦向きの情報 | 映像上に下部情報を重ねる | 情報領域を高さ35%分確保 | 縦長ウィンドウで映像上に下部情報を重ねる |
| 音声のみ | iOSの波形／Windowsの中央タイトルに相当する専用中央UIは、この画面定義にない | 中央に波形マーク | 中央に音声アイコンとファイル名 |
| バッファリング表示 | PlayerViewの標準表示を無効化 | 「再生準備中」「バッファリング中」のProgressView | 下部に読み込み等の状態文 |
| 詳細情報 | ダイアログ。音声出力とUSBミキサー設定状態 | セクション付きの情報シート | スクロール可能な情報ダイアログ |
| 動画中のマウスカーソル | 専用の自動非表示処理は比較対象コードで確認できない | iPhone／iPadのタッチUIが中心 | マウス操作が3秒ないと非表示 |

iOSの縦画面は情報用の領域が映像と別に確保されるため、同じ縦長画面でも動画領域の割り当てが違う。一時停止時はiOSの方が画面上のボタンだけで前後移動・シークを発見しやすい構成。

根拠：[Android画面](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/res/layout/activity_main.xml)、[iOSプレイヤー](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/PlayerScreen.swift)、[Windows画面](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.xaml)、[Windowsプレイヤー](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Player.cs)、[Windowsカーソル仕様](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/docs/spec.md)

## 4. 再生開始までの操作

### 端末内／USB

- **Android**：フォルダー追加でSAFのアクセス許可を取得すると、そのフォルダーのアプリ内一覧を直接開く。保存済みがSAFの1件だけなら、ホームの選択ボタンから一覧を省略してそのフォルダーへ進む。Android TVは独自USBブラウザーも選べ、全ファイルアクセスの許可が必要。
- **iOS**：標準ファイル画面でフォルダーを追加し、保存済み一覧から登録先を開き、再生するファイルを選ぶ。USBも標準ファイル画面から扱う。登録済みが1件でも保存済み一覧を表示する。
- **Windows**：PCの標準フォルダーピッカー、またはドライブ一覧からフォルダーへ移動し、「このフォルダを使用」で登録。保存済み一覧からファイルを選ぶ。ドライブ／追加用ブラウザーから登録前にファイルを選んで再生する経路もある。

フォルダーの登録自体では3版とも再生を開始しない。

根拠：[Android選択フロー](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/README.md)、[Android保存済み選択分岐](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/java/jp/nagu/continuousplayer/SafFileBrowser.kt#L60)、[iOS画面遷移](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/ContentView.swift)、[Windows追加処理](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Browser.cs#L164)

### NAS／DLNA

- **Android・Windows**：SSDPでサーバーを自動探索→サーバー→フォルダー→開始ファイル。アドレス手入力用の画面は比較対象実装にない。
- **iPhone／iPadの通常実機ビルド**：IPアドレス・ホスト名などを入力して接続。自動検索の可否は署名権限とビルド設定に依存する。SimulatorとMac Catalystには自動検索経路がある。
- **DLNAで表示できる対象も違う**：Android・Windowsは動画／音声リソースを扱うが、iOSリポジトリのDLNA実装はMP4／M4Vが対象。NAS上の音楽ファイルは同じようには選べない。
- iOSも実装上は登録前のDLNAファイル行から直接再生できる。READMEの「登録してから選ぶ」は代表的な手順であり、必須条件ではない。

根拠：[Android DLNA仕様](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/docs/spec.md)、[Windows一覧処理](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Browser.cs)、[iOS検索可否](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/DLNADiscovery.swift)、[iOS DLNA選択](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/DLNABrowser.swift)、[iOS対象リソース](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/DLNAClient.swift#L290)

## 5. 再生中の基本操作

以下のAndroid矢印操作は「再生中、または一時停止ツールバーにフォーカスがない場合」。Apple TVのスワイプは後述。

| 操作 | Android | iPhone／iPad＋外部キーボード | Windows |
|---|---|---|---|
| 左／中央／右をタップ | −10秒／再生切替／＋10秒 | 同じ。ただし映像領域が対象 | 同じ。マウス操作も対象 |
| 右／左スワイプ | 前／次 | 前／次 | 前／次 |
| ←／→ | 前／次 | 前／次 | 前／次 |
| ↑／↓ | −10秒／＋10秒 | −10秒／＋10秒 | −10秒／＋10秒 |
| 再生切替キー | 決定／Enter、メディアキー | Space | Space／Enter、メディアキー |
| Shift＋←／→ | 専用処理なし | −10秒／＋10秒 | 修飾キーによる別の割当なし |
| 情報 | INFOキー | I | I |
| 全画面切替 | 再生時にシステムバー非表示 | 再生時にシステムUI非表示 | 再生開始で全画面。F11で切替 |

### 操作感の違い

1. **一時停止中の矢印キー**：Android TVはツールバーにフォーカスが移り、矢印はボタンの選択に使われる。Windowsは停止中も左右＝ファイル送り、上下＝シークを維持し、ボタン移動にはTab／Shift+Tabを使う。iOSは外部キーボードの再生コマンドを維持する。
2. **ダブルタップ**：Android／iOSは専用のダブルタップで1回分の処理を行う。Windowsは解放イベントごとに処理するため2回分になる。例えば右側ダブルタップは、Android／iOSでは＋10秒、Windowsでは＋10秒を2回実行する。末尾に近い場合は曲送り条件にも影響する。中央ダブルタップもWindowsは2回切り替える。
3. **スワイプ判定**：Androidは距離100px超・速度100px/秒超、Windowsは100DIP超・100DIP/秒超。iOSは横70pt超かつ横移動が縦移動の1.5倍超で、同じ速度条件はない。短いスワイプやゆっくりしたスワイプの反応は一致しない。
4. **一覧のページ移動**：Androidのファイル一覧ではD-pad左右がPage Up／Downとして働く。Windowsの左右はその用途に割り当てられておらず、Alt+←で親へ、F5で再読込。iOS一覧はBackspaceで親へ戻る。

根拠：[Androidキー処理](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/java/jp/nagu/continuousplayer/MainActivity.kt#L201)、[Androidジェスチャー](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/java/jp/nagu/continuousplayer/GestureHandler.kt)、[iOSジェスチャー](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/PlayerScreen.swift#L174)、[iOSキー処理](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/PlaybackSurface.swift#L61)、[Windowsキー処理](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.xaml.cs#L93)、[Windowsポインター処理](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Player.cs#L309)

## 6. 戻る・選び直す・キャンセル

| 場面 | Android | iPhone／iPad | Windows |
|---|---|---|---|
| 再生中にフォルダーボタン | プレイヤーを解放し再生元一覧へ | 一時停止した動画の上に一覧を重ねる | プレイヤーを解放し再生元一覧へ |
| そこでキャンセル | 元の動画の再生位置へ戻る経路はない | 元の動画の一時停止位置へ戻れる | 元の動画の再生位置へ戻る経路はない |
| システムの戻る／Esc | スマホの戻るはホーム。TVの戻るは再生元一覧 | iPhone／iPadの再生画面にEscの一覧表示割当なし。Macにはある | Esc／Oで再生元一覧 |
| ホーム操作 | 再生を終了してホーム | 再生を終了してホーム | 再生を終了してホーム |
| 再起動後 | 登録フォルダー等を保持。再生秒数は保存しない | 登録フォルダー等を保持。再生秒数は保存しない | 登録先・再生元・ファイルIDを保存し、一覧選択にも利用。再生秒数・プレイリストは復元しない |

**「別のファイルを探すだけ」の操作で再生位置を保持できるかが、大きな体験差。**

根拠：[Androidフォルダーボタン](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/java/jp/nagu/continuousplayer/MainActivity.kt#L163)、[Android戻る](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/java/jp/nagu/continuousplayer/MainActivity.kt#L179)、[iOS一覧オーバーレイ](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/ContentView.swift#L137)、[Windows一覧へ戻る](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Player.cs#L261)、[Windows一覧位置復元](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Browser.cs#L142)

## 7. エラー・音声出力

- **Android**：TV、またはHTTP系の再生ではエラーを画面に出し、ユーザーに対応を選ばせる。スマホのローカル再生では自動スキップの経路がある。端末種別・再生元で挙動が違う。
- **iOS**：通常の再生失敗は次へ進み、連続3件または末尾の失敗で停止して理由を表示。アクセス失効などはフォルダーの再選択を案内する。
- **Windows**：自動スキップせず、再試行／別ファイル／キャンセルのダイアログを表示する。
- **音声出力情報**：Androidは対応USB DACのビットパーフェクト設定を試み、設定状態を表示。iOSはAVAudioSessionの出力情報を表示。Windowsは標準ミキサー経由で、WASAPI排他・ビットパーフェクト設定UIはない。

音声出力の表示値や設定成功は、実際のビット一致を測定した結果ではない。

根拠：[Androidエラー処理](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/java/jp/nagu/continuousplayer/MainActivity.kt#L395)、[Androidコントローラー](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/java/jp/nagu/continuousplayer/PlayerController.kt)、[iOS仕様](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/docs/spec.md)、[Windowsエラー・情報](https://github.com/mnagata/ContinuousPlayer_for_Win/blob/62806952a521cada0e6b018734041c3768ceba81/ContinuousPlayer/MainPage.Player.cs#L233)

## 8. 同じリポジトリ内のMac／TVも含める場合

### Mac Catalyst

- iPhone／iPadのSwiftUI画面を共有するが、再生操作は通常のタイトルバーに常時表示する。
- 一時停止中も画面下の再生操作ボタン・キー案内を出さない。
- Escで一時停止して一覧を重ね、キャンセルで元の動画へ戻れる。
- ウィンドウが非アクティブになっただけでは再生を止めない。Windowsは非アクティブになると一時停止する。
- 再生開始で強制的に全画面にするWindowsとは異なるウィンドウ運用。macOS標準のヘルプもある。

### Android TVとApple TV

- Android TVはスマホと同じAPK・共通レイアウト、Apple TVは専用のホーム・下部操作パネルを持つ。
- Android TVはSAF／USB／DLNA、Apple TVはDLNAのみ。
- **Apple TVは左スワイプ＝前、右スワイプ＝次。iPhone／iPadの「左＝次、右＝前」と逆。**
- Apple TVは再生中の左右ボタン／左右タップでは曲送りをせず、実際のスワイプを区別する。
- Apple TVの戻るは、まず操作ボタンを表示し、操作表示中のもう一度の戻るで一覧へ移る。Android TVは戻る1回で再生を終了して一覧へ移る。
- Apple TVの10秒シークは操作パネルのボタンから実行する。

根拠：[iOS README（Mac）](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/README.md)、[iOS仕様（Mac・TV）](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/docs/spec.md)、[Apple TV実装](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_tvOS/TVPlayerScreen.swift)

## 9. 資料・案内と実装の不一致

### Androidのリモコン案内は修正候補

- ホームのtv_controls_hint：Left/Right seek、Up/Down previous/next。
- docs/spec.mdのリモコン表も同じ旧割当。
- **現在のMainActivityは、左右＝前／次、上下＝−10秒／＋10秒。READMEは現在の実装と合う。**

案内を信じるとシークのつもりで別ファイルへ移動してしまう。画面内の案内と仕様書を現在の実装に揃える優先度が高い。

根拠：[画面内案内の文字列](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/res/values/strings.xml#L11)、[現在のキー割当](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/app/src/main/java/jp/nagu/continuousplayer/MainActivity.kt#L244)、[README](https://github.com/mnagata/ContinuousPlayer_for_Android/blob/0267a9902e08c5830886a0ee74025020e65d9e4a/README.md)

### iOSのdocs/ui.mdは履歴として読む必要がある

旧システムファイル選択、ホームの複数ボタン、拡張子表示など、途中段階の説明が累積している。現在の主動線はContentView・MediaFileBrowser・DLNABrowserとREADME／docs/spec.mdで確認した。旧記載だけから現在のUIを判断すると誤る。

根拠：[UI記録](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/docs/ui.md)、[現行ContentView](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/ContinuousPlayer_for_iOS/ContentView.swift)、[現行仕様](https://github.com/mnagata/ContinuousPlayer_for_iOS/blob/8ec1593e2cd5724158834c6d305c3b11cdd8906d/docs/spec.md)

## 10. 統一を検討する場合の順序

1. Androidのリモコン案内を現在の割当に合わせる。
2. フォルダー再選択で再生位置を保持するか、終了するかを決める。
3. ダブルタップを1回分とするか2回分とするかを揃える。
4. 拡張子表示・長い名前の行数・読み込み中の表示を決める。
5. iOSのDLNA手入力とAndroid／Windowsの自動探索を踏まえ、共通の接続導線を検討する。
6. Apple TVも同じ操作体系にするなら、スワイプ方向を含めて方針を決める。

上記は調査結果からの提案であり、コード変更は実施していない。
