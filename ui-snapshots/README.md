# UIデザイン・スナップショット

各プラットフォームの**実装時点の見た目**を、同じ画面ID・状態IDで比較するための資料です。共通操作の採用仕様は[共通操作仕様](../docs/common-operation-spec.md)を参照してください。画像の存在は仕様への準拠や実機動作の保証を意味しません。

## プラットフォーム別の収録状況

| プラットフォームID | 対象 | 収録記録 | 状態 |
|---|---|---|---|
| `android` | Android スマートフォン・タブレット | [2026-11-01：スマートフォン、縦・横](android/2026-11-01/README.md) | サンプルデータによる実UI、15枚。タブレットは未収録 |
| `android-tv` | Android TV | [2026-11-01：1080p、D-padフォーカス](android-tv/2026-11-01/README.md) | TVエミュレーターの実UI、17枚 |
| `ios` | iPhone / iOS | [2026-11-01：縦・横](ios/2026-11-01/README.md) | Simulator実UI、16枚 |
| `ipados` | iPad / iPadOS | [2026-11-01：縦・横、シート](ipados/2026-11-01/README.md) | Simulator実UI、16枚 |
| `tvos` | Apple TV / tvOS | [2026-11-01：1080p、リモコン](tvos/2026-11-01/README.md) | Simulator実UI、12枚。音声情報は未収録 |
| `mac-catalyst` | Mac Catalyst | [2026-11-01：ウィンドウ・シート](mac-catalyst/2026-11-01/README.md) | Mac実UI、16枚。JPEG原本と同寸法PNG |
| `macos` | macOSネイティブ | — | 未収録。実装の存在を示すものではない |
| `windows` | Windows | [2026-11-03：ウィンドウ・ダイアログ](windows/2026-11-03/README.md) | Windows実UI、16枚。JPEG原本と同寸法PNG |

同じアプリのコードを共有していても、TV・タブレット・デスクトップの撮影記録は分けます。Mac CatalystとmacOSネイティブも別IDで扱います。

## 資料の使い方

- Apple 4版を比較する：[2026-11-01の比較入口・60枚](apple-2026-11-01.md)
- 画面を見る：[Android](android/2026-11-01/README.md)、[Android TV](android-tv/2026-11-01/README.md)、[Windows](windows/2026-11-03/README.md)
- 他OSを追加する：[記録形式・更新ルール](format.md)、[追加用テンプレート](templates/capture-record.md)
- 自動処理する：[プラットフォーム台帳](catalog.json)、各記録の `manifest.json`
- 撮影を再現する：[Android撮影手順](../tools/android/README.md)、[Apple版撮影手順](../tools/apple/README.md)、[Windows版撮影手順](../tools/windows/README.md)

収録後にUIが変わったら、新しい撮影IDのディレクトリを追加します。旧画像は履歴資料として残し、この一覧と `catalog.json` の最新記録へのリンクを更新します。プラットフォーム間でUIを一律に揃える判断は、この資料から自動的には行いません。

## 過去の記録

- 2026-10-27：[Android 15枚](android/2026-10-27/README.md)、[Android TV 16枚](android-tv/2026-10-27/README.md)

2026-11-01版では、ホームの英字見出し・選択ボタンと音声出力情報の更新を反映し、TVの情報画面の下部を追加収録しています。
