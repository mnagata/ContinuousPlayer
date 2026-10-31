# Android UIスナップショットの再撮影

[2026-11-01記録](../../ui-snapshots/android/2026-11-01/README.md)の撮影補助です。Androidアプリの製品ソースを変更せず、既存の `BrowserTestUi` と実際のActivity・ブラウザー・プレイヤー・ダイアログを使います。新たなアプリ機能や一般的な回帰テストではありません。

## 前提

- Androidの参照コミット：`85ff21122442aa6ca6ed200d11c177f6e806ef60`。後の版ではprivateメソッド・画面ID等を確認し、ハーネスを適合させます。
- Android SDK 36.1、JDK 21、既存のGradle依存キャッシュ、`ffmpeg`（libx264）、Bash。
- 起動済みの**撮影専用エミュレーター**。収録条件は1080×2400px、420dpi、文字倍率1.0、OS英語。アプリのデフォルトリソースは日本語です。
- SAFの永続アクセス許可がない環境を使います。個人の保存先がある環境では、保存済み一覧へ写り込む可能性があります。

```bash
export ANDROID_HOME="$HOME/Library/Android/sdk"
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
bash tools/android/capture.sh /path/to/ContinuousPlayer_for_Android emulator-5554 /tmp/cp-ui-new android 2026-11-01
```

スクリプトは一時的な撮影用Kotlinソースと幾何学模様の90秒動画をAndroid側の `androidTest` へ配置し、Debug APKとテストAPKをビルド・更新インストールして実行します。終了時にホスト側の一時ソース・メディアを削除します。PNGは指定した出力先の `files/ui-snapshots-<capture-id>/` に取得します。スクリプトは共通仕様リポジトリへの画像の登録や上書きを行いません。

## 状態の準備と制約

- `UI Sample` に動画、48kHz・mono・16bit・90秒の無音WAV、同名異形式のWAV、子フォルダーを用意します。動画は640×360・24fps・音声なしです。
- 保存済み一覧の表示用に、アプリ専用ディレクトリをUSB形式の保存先モデルへ登録します。実USBの権限取得・マウント検証ではありません。
- ファイル一覧は、撮影用に開始した再生から「ファイル一覧」で開く `UsbPlaybackBrowser` の実画面です。実ストレージのアクセス権は追加しません。
- 再生開始は `startPlayback` をリフレクションで呼び、実際のプレイヤーでサンプルを再生します。一時停止・情報・一覧・ホーム等は実際のView操作またはタッチで実行します。
- 終了は音声の末尾付近へのシーク後、実際の再生終了を待ちます。エラーは不正なMP4サンプルによる実デコード失敗です。
- OSの初回全画面案内が出た場合は「Got it」を通常のクリックで閉じてから撮影します。OS設定やストレージ権限は変更しません。
- ハーネスは登録先・最後のフォルダー等のSharedPreferencesを退避し、`finally` で復元し、サンプルファイルを削除します。APKは更新され、プロセスは再起動されるため、日常利用中の端末には実行しないでください。強制停止・OSクラッシュでは後処理が完了しない可能性があります。

撮影に失敗すると部分的なPNGをアプリの `files/ui-snapshots-<capture-id>/` に残します。必要な画像を取得してから、当該撮影用ディレクトリだけを削除して再実行してください。既存のサンプル／出力ディレクトリがある場合、ハーネスは上書きせず失敗します。

撮影用動画は `make-fixture.sh` から生成するため、第三者の映像・音源を含みません。ffmpegやOSの版、エンコーダー、ステータスバー時刻によりPNGやメディアのバイト列は変わり得ます。再撮影画像は新しい撮影IDに保存し、メタデータとハッシュを更新してください。

## Android TVの撮影

[Android TVの記録](../../ui-snapshots/android-tv/2026-11-01/README.md)には `TvUiSnapshotCaptureTest.kt` を使用します。スマートフォンの画面を拡大したり、アプリの `uiMode` を差し替えたりせず、Android TVシステムイメージ上で実行します。

Android TV API 36・arm64-v8aの撮影専用エミュレーターを起動し、1920×1080px、320dpi、文字倍率1.0、D-pad入力を使用してください。`adb -s <serial> shell am get-config` に `land-television` が含まれることを確認します。

```bash
bash tools/android/capture.sh /path/to/ContinuousPlayer_for_Android emulator-5560 /tmp/cp-tv-ui-new android-tv 2026-11-01
```

第4引数はプラットフォーム（省略時は `android`）、第5引数は撮影ID（省略時は `2026-11-01`）、第6引数は確認済みの完全な参照コミット（省略時は `85ff21122442aa6ca6ed200d11c177f6e806ef60`）です。異なるコミットでは、ハーネスとの整合を確認してから明示指定してください。誤って実機へ実行しないよう、撮影スクリプトは `emulator-` で始まるシリアルを必須にしています。PNGと撮影ログに加え、OS構成値・APKハッシュを出力します。

TV撮影ではホームの選択ボタン、空の保存済み一覧、登録済み一覧、解除確認、USB入口と権限案内、再生ボタン／情報ボタンのフォーカス、ファイル一覧の行選択、メディア情報、終了・エラーを収録します。操作にはD-padの上下・決定・戻る・Escを注入します。一部の撮影開始位置は `requestFocus()` で指定し、登録解除の確認は既存ListViewの長押しコールバックを呼んで表示します。物理リモコンの長押し時間やキーマッピングの検証ではありません。

各PNG撮影時の最前面ウィンドウのフォーカスID・文字列とUTC時刻を `capture-details.json` に保存します。ファイル一覧は撮影用の再生セッションから開き、今回の条件では最初に子フォルダー行、↓操作後にMP4の行を選択した状態です。画像名の `variant` とこの記録を対応させます。

USB入口を選んだ後のアクセス案内だけを撮影し、「設定を開く」は実行しません。ストレージ権限の付与や実USB・NASへの接続は行いません。エラー画面の「再試行」は撮影準備でフォーカスを指定しています。

### 今回のTVエミュレーター設定

- Google配布の [Android TVシステムイメージ一覧](https://dl.google.com/android/repository/sys-img/android-tv/sys-img2-1.xml) の `system-images;android-36;android-tv;arm64-v8a`、revision 4。
- 配布ファイル：[arm64-v8a-36_r04.zip](https://dl.google.com/android/repository/sys-img/android-tv/arm64-v8a-36_r04.zip)、SHA-1 `ceb1dfcc6a28dbd9dbade780428cf7bafb1c65ef`。
- 今回は一時ディレクトリへ展開し、撮影専用AVDを作成しました。通常はAndroid StudioのSDK管理機能で同じパッケージを用意できます。
- AVDの主要値：`hw.lcd.width=1920`、`hw.lcd.height=1080`、`hw.lcd.density=320`、`hw.initialOrientation=landscape`、`hw.dPad=yes`、`hw.keyboard=yes`、`hw.mainKeys=yes`、`hw.cpu.arch=arm64`、`hw.cpu.ncore=4`、`hw.ramSize=2048`。実際のOS構成・機種は各撮影記録のmanifestへ残します。
- Emulator 37.2.12.0、`-no-snapshot -no-boot-anim -no-window -no-audio` で起動しました。撮影後にエミュレーターを終了します。

## 2026-11-01の更新

ホーム文言・音声出力情報が変わった現行版を撮影対象にしました。スマートフォンでもPNGごとの時刻と実フォーカスを保存します。音声情報は下端へスクロールした画面も収録し、内容が一画面に収まるスマートフォンでは重複した画像を作りません。TVは上端・下端の2枚で、出力経路と新しい注記まで記録します。ミキサー/HAL診断用の追加権限は付与せず、取得不可の実表示を収録します。

[2026-10-27 Android](../../ui-snapshots/android/2026-10-27/README.md)・[2026-10-27 Android TV](../../ui-snapshots/android-tv/2026-10-27/README.md)の画像とmanifestは過去の記録として保持します。撮影補助コードは最新の参照版へ更新されているため、過去の記録のハーネスハッシュとは異なります。
