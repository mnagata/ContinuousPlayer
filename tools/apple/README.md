# Apple版 UIスナップショットの再撮影

対象はiOS、iPadOS、tvOS、Mac Catalystです。[2026-11-01の比較入口](../../ui-snapshots/apple-2026-11-01.md)と[記録形式](../../ui-snapshots/format.md)を参照してください。製品UIは描き直さず、固定コミットをビルドして通常の選択・再生経路を操作します。

## 必要なもの

- Xcodeと対象Simulatorランタイム、Python 3、ffmpeg（libx264 / AACを含む）
- `ContinuousPlayer_for_iOS`のローカルリポジトリ
- Macの手動撮影にはComputer Useなど、対象アプリのウィンドウ・シートを取得できる撮影手段

2026-11-01版は参照コミット`a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5`、Xcode 27.1で撮影しました。iPhone / iPadは26.3.1、Apple TVは27.0のランタイムです。実機・実NAS・USB DACの確認とは区別してください。

登録の初期化は`--ui-bookmark-tests --ui-reset-registrations`による`uitests.`キーだけが対象です。一方、`dlna.lastServerAddress`は製品と共通の設定キーで、テストURLへの接続によって更新されます。通常利用から独立したSimulatorまたはmacOSユーザーで再撮影してください。今回も通常の登録や個人ファイルの削除は行っていません。

## 作業コピーとサンプルの準備

パスは自分の環境へ置き換えます。以下の`TOOLS`はこの共通仕様リポジトリの`tools/apple`、`SOURCE`はアプリリポジトリです。

```bash
export DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer
TOOLS=/path/to/ContinuousPlayer/tools/apple
SOURCE=/path/to/ContinuousPlayer_for_iOS
WORK=$(mktemp -d /tmp/continuousplayer-ui.XXXXXX)

git clone --local --no-hardlinks "$SOURCE" "$WORK/source"
git -C "$WORK/source" checkout --detach a4c40408350ca1d2e0b9bee33c09f6d8bc14d5a5
cp "$TOOLS/SnapshotCaptureTests.swift" "$WORK/source/DeviceUITests/"
cp "$TOOLS/SnapshotCaptureTests.swift" "$WORK/source/TVUITests/"
python3 "$TOOLS/generate-media.py" "$WORK/media"
python3 "$TOOLS/fixture-server.py" "$WORK/media" --port 18769
```

サーバーは`127.0.0.1`のみにbindし、外部NASやSSDPを使いません。サーバーのターミナルを維持し、別ターミナルで同じ`WORK`等を設定して撮影します。終了時はサーバーをCtrl+Cで止めます。アプリ製品ソース・Xcodeプロジェクトへの変更は不要です。

サンプルは90秒の幾何学模様動画（640×360、24fps、H.264、音声なし）、無音AACのみのMP4（90秒・2秒、48kHz・mono）、破損MP4です。動画の別名M4Vと空の子フォルダーはサーバーが一覧に提示します。WAVはAACの生成元で、DLNA一覧は固定コミットが対象とするMP4/M4Vを使用します。

## iPhone / iPad / Apple TVの撮影

`xcrun simctl list devices available`でUDIDを確認します。iPhoneとiPadは`DeviceValidation`、Apple TVは`ContinuousPlayer_for_tvOS`スキームを使います。起動済みアプリの通常データを撮影に混ぜないよう、ハーネスが専用登録キーを初期化します。`--ui-session-state`は指定せず、デバッグ用の状態文字を画像に出しません。

```bash
# iPhone（iPadも同じ。UDIDと出力先を変えて別実行）
xcodebuild -project "$WORK/source/ContinuousPlayer_for_iOS.xcodeproj" \
  -scheme DeviceValidation \
  -destination 'platform=iOS Simulator,id=<IPHONE-OR-IPAD-UDID>' \
  -derivedDataPath "$WORK/ios-build" \
  -resultBundlePath "$WORK/iphone.xcresult" \
  -only-testing:DeviceUITests/SnapshotCaptureTests \
  -parallel-testing-enabled NO test CODE_SIGNING_ALLOWED=NO

# Apple TV
xcodebuild -project "$WORK/source/ContinuousPlayer_for_iOS.xcodeproj" \
  -scheme ContinuousPlayer_for_tvOS \
  -destination 'platform=tvOS Simulator,id=<APPLE-TV-UDID>' \
  -derivedDataPath "$WORK/tv-build" \
  -resultBundlePath "$WORK/tv.xcresult" \
  -only-testing:TVUITests/SnapshotCaptureTests \
  -parallel-testing-enabled NO test CODE_SIGNING_ALLOWED=NO
```

撮影経路は[SnapshotCaptureTests.swift](SnapshotCaptureTests.swift)にあります。ホームからフォルダーを登録して動画を選び、実プレイヤーで再生・一時停止・情報表示します。音声・終了・破損は`audio.xml`・`end.xml`・`failure.xml`の別キューで撮影します。iPhone / iPadはホーム・一覧・一時停止の横向きも撮影します。

tvOSの音声情報は、この収録ではハーネスのリモコンによるフォーカス移動が到達しなかったため除外しています。音声再生画面は収録します。動画情報の下部スクロールも収録していません。ボタン・セル・入力欄の`hasFocus`を添付JSONに保存し、空文字を映像surfaceのフォーカス不在と解釈しません。

## PNGを資料へ取り込む

```bash
xcrun xcresulttool export attachments \
  --path "$WORK/iphone.xcresult" --output-path "$WORK/iphone-export"
python3 "$TOOLS/export-attachments.py" \
  "$WORK/iphone-export" /path/to/ContinuousPlayer/ui-snapshots/ios/<new-capture-id>
```

iPad / tvOSも同じように別ディレクトリへ出力します。エクスポート補助は画像をコピーするだけで、回転・合成・リサイズをしません。既存のPNGを上書きしません。撮影時刻とフォーカスは`capture-details.json`、テスト成功結果は`capture-result.txt`へ残します。

XCUIScreenの横向きPNGは、IHDRが縦寸法のままでEXIF orientation=8を持つことがあります。原画像を保持し、`width_px` / `height_px`にはIHDR、`environment_overrides.display_width_px` / `display_height_px`にはEXIFを適用した表示寸法を記録します。対応しないビューアー向けに回転画像を作る場合は`derived/`に分けてください。

各記録の`manifest.json`へ完全コミット、製品差分、ビルド、実行環境、サンプル、画像寸法・ハッシュ・操作・観察・未収録範囲を記入します。`artifact-files.json`はアプリバンドル内のファイル別SHA-256一覧で、ソースの`artifact_sha256`はその一覧から計算したbundle tree digestです。Debug版ではコードが`.debug.dylib`に分かれるため、実行ファイルだけでなくバンドルの全ファイルを対象にします。

## Mac Catalyst

固定コミットに既存のビルドスクリプトを使います。

```bash
cd "$WORK/source"
BUILD_DIR="$WORK/mac-build" ./script/build_and_run.sh --build-only
open -n "$WORK/mac-build/Build/Products/Debug-maccatalyst/ContinuousPlayer.app" --args \
  --ui-bookmark-tests --ui-reset-registrations --ui-dlna-direct-only \
  -dlna.lastServerAddress http://127.0.0.1:18769/video.xml \
  -AppleLanguages '(ja)' -AppleLocale ja_JP
```

通常UIをポインターとSpaceキーで操作します。ホーム → 登録 → ファイル一覧 → Sample OP.mp4 → Space → メディア情報を撮影します。保存済み行の右クリックで解除確認を開き、撮影後はキャンセルします。Other Folderで空一覧を収録します。再生画面のファイル一覧から保存済みへ戻って追加先を開き、接続先を`audio.xml` / `end.xml` / `failure.xml`へ変更し、対応するサーバーとフォルダーを登録して再生します。

今回のComputer Useは`app.getScreenshot()`で取得したJPEGを`source-jpeg/`へ保存しました。同じピクセル寸法のPNGを次で作り、PNGと取得元JPEGの両方のSHA-256をmanifestへ記録しています。JPEGの圧縮品質はツール依存で未測定です。

```bash
sips -s format png source-jpeg/<name>.jpg --out images/<name>.png
```

情報・解除確認は撮影ツールが返すネイティブシート領域だけです。全ウィンドウ画像と混同せず、`desktop-sheet` / `desktop-window`などのvariant・寸法・`surface`を記録します。情報シートは478×524px、解除確認は260×122pxで、`surface: native-sheet-only`も記録しています。画像の合成は行いません。

## 最終確認

画像をすべて目視し、回転途中・未準備状態・別アプリ・個人データの混入、説明と画像の対応を確認します。実際の表示上の差や撮影できなかった状態は`coverage`と`limitations`に残します。入口の一覧と`catalog.json`を更新し、共通仕様リポジトリで以下を実行します。

```bash
python3 tools/validate_ui_snapshots.py
```
