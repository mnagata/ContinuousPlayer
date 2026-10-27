# ContinuousPlayer

ContinuousPlayer各プラットフォームの共通仕様・設計資料を管理するリポジトリです。

対象はAndroid、Android TV、iOS、iPadOS、tvOS、Mac Catalyst、Windowsです。

## ドキュメント

| 文書 | 内容 | 状態 |
|---|---|---|
| [共通操作仕様案 v1](docs/common-operation-spec.md) | ファイル選択・キャンセル、再生操作、フォーカス、中断・復帰、登録、エラー処理と共通の確認項目 | 提案・未実装 |
| [UI・操作比較（2026年10月27日）](docs/platform-ui-operation-comparison-2026-10-27.md) | 現行Android版を反映した3版の比較。上下端の操作部、選び直し・入力・DLNA・エラーと共通仕様に残る差 | 固定コミットの実装比較 |
| [UI・操作比較（2026年10月24日）](docs/platform-ui-operation-comparison-2026-10-24.md) | Android・iOS・Windowsのコード調査とMac／TV版の差分。参照コミット・根拠へのリンク付き | 調査時点の記録 |

共通操作仕様はこのリポジトリの [docs/common-operation-spec.md](docs/common-operation-spec.md) を管理元とします。比較資料は調査日時点の記録であり、最新の実装状況や動作保証を示すものではありません。

## アプリのリポジトリ

| リポジトリ | 対象 |
|---|---|
| [ContinuousPlayer_for_Android](https://github.com/mnagata/ContinuousPlayer_for_Android) | Android・Android TV |
| [ContinuousPlayer_for_iOS](https://github.com/mnagata/ContinuousPlayer_for_iOS) | iOS・iPadOS・tvOS・Mac Catalyst |
| [ContinuousPlayer_for_Win](https://github.com/mnagata/ContinuousPlayer_for_Win) | Windows |

## 更新方針

- 共通操作の変更は、このリポジトリの仕様書を更新して履歴を残します。
- 提案・採用済みの仕様と、各アプリへの実装済みの状態を区別して記録します。
- 各アプリの実装・検証は、それぞれのリポジトリで管理します。
