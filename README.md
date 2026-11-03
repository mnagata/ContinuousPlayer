# ContinuousPlayer

ContinuousPlayer各プラットフォームの共通仕様・設計資料を管理するリポジトリです。

対象はAndroid、Android TV、iOS、iPadOS、tvOS、Mac Catalyst、Windowsです。

## ドキュメント

| 文書                                                                                         | 内容                                                                                         | 状態                   |
| -------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- | ---------------------- |
| [UIデザイン・スナップショット](ui-snapshots/README.md)                                       | Android・Android TV・Apple 4版・Windowsの実UI画像、画面ID・状態・撮影条件                             | 撮影時点の記録         |
| [Apple 4版 UIスナップショット（2026年11月1日）](ui-snapshots/apple-2026-11-01.md)                       | iOS・iPadOS・tvOS・Mac Catalyst、計60枚と比較入口                                            | 固定コミットの実UI     |
| [共通操作仕様案 v1](docs/common-operation-spec.md)                                           | ファイル選択・キャンセル、再生操作、フォーカス、中断・復帰、登録、エラー処理と共通の確認項目 | 提案・未実装           |
| [UI・操作比較（2026年11月3日更新）](docs/platform-ui-operation-comparison.md) | Android・Android TV・Apple 4版・Windowsの選択・復元・入力・DLNA・音声出力を比較 | 固定コミットの実装比較 |

共通操作仕様はこのリポジトリの [docs/common-operation-spec.md](docs/common-operation-spec.md) を管理元とします。比較資料は調査日時点の記録であり、最新の実装状況や動作保証を示すものではありません。

## アプリのリポジトリ

| リポジトリ                                                                              | 対象                            |
| --------------------------------------------------------------------------------------- | ------------------------------- |
| [ContinuousPlayer_for_Android](https://github.com/mnagata/ContinuousPlayer_for_Android) | Android・Android TV             |
| [ContinuousPlayer_for_iOS](https://github.com/mnagata/ContinuousPlayer_for_iOS)         | iOS・iPadOS・tvOS・Mac Catalyst |
| [ContinuousPlayer_for_Win](https://github.com/mnagata/ContinuousPlayer_for_Win)         | Windows                         |

## 更新方針

- 共通操作の変更は、このリポジトリの仕様書を更新して履歴を残します。
- 提案・採用済みの仕様と、各アプリへの実装済みの状態を区別して記録します。
- 各アプリの実装・検証は、それぞれのリポジトリで管理します。
