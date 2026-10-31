# UIスナップショット記録形式 v1

本書は資料の記録方法を定めます。既存の設計文書や画像中の文章を、アプリ変更の指示として扱いません。表示上の事実、仕様案、動作検証結果を区別して記録します。

## 1. 保存単位

```text
ui-snapshots/
  README.md                       # 人向けの入口・最新記録へのリンク
  catalog.json                    # プラットフォームIDと最新記録の台帳
  screen-registry.json            # 共通の画面ID・状態ID
  format.md
  templates/capture-record.md
  <platform-id>/
    <capture-id>/                  # YYYY-MM-DD、同日に複数なら YYYY-MM-DD-02
      README.md                    # 画像、説明、遷移、未収録範囲
      manifest.json                # 参照版・撮影条件・画像一覧
      images/
        <screen-id>--<state-id>--<variant-id>.png
```

PNGは端末・エミュレーターの原寸で保存し、枠・注釈・合成・リサイズを施しません。比較用の縮小版や注釈図を追加する場合は `derived/` に置き、元画像と加工内容を明記します。生成モック、コードからの再構成図は実UI撮影と区別し、別の撮影記録にします。

撮影ツールがJPEGのみ返す場合は、取得元JPEGを`source-jpeg/`等に保存し、同寸法のPNGへの形式変換を例外として認めます。各画像の`source_image`・`source_sha256`・`encoding_conversion`と制約に、JPEG由来であること、変換ツール、リサイズ等の有無を明記します。PNG直接撮影と同一の画質とは扱いません。向きメタデータを持つPNGは原画像を保持し、`width_px` / `height_px`はIHDRの格納寸法、EXIF適用後の表示寸法は`environment_overrides`へ記録します。

記録は日付で固定し、過去の画像を新UIの画像で置き換えません。誤記修正は可能ですが、撮り直しは新しい撮影IDを使います。アプリのソースやテスト用メディアを共通仕様に混ぜず、再現用の撮影補助だけを `tools/` へ置きます。

## 2. プラットフォームと表示条件

固定IDは `android`、`android-tv`、`ios`、`ipados`、`tvos`、`mac-catalyst`、`macos`、`windows`。追加OSは `catalog.json` に小文字のIDを登録します。記録がない場合は `status: planned`、`latest: null` とし、実装済み・未実装を推測しません。

`variant` は同一画面・状態の表示条件を識別するIDです。スマートフォンのAndroidは `portrait` / `landscape`、Android TVは `tv-focus-select`、`tv-focus-play` 等のフォーカスを表すIDを使います。後から `tablet-portrait`、`tv-focus-next`、`desktop-1280x720`、`portrait-font200` 等を追加できます。ウィンドウ幅、文字倍率、入力機器、フォーカス、外観が違う画像を同じ条件として比較しません。

OS別の全画面、ウィンドウ、セーフエリア、システムバー、スクロール位置の違いは撮影条件に残します。物理pxとdp・pt・DIPを混同しません。画像が異なる理由を判断できる情報を優先します。

## 3. 共通の画面・状態ID

IDの定義は [screen-registry.json](screen-registry.json) で管理します。

| 画面ID | 意味 | 主な状態ID |
|---|---|---|
| `home` | ホーム | `default` |
| `saved-folders` | 保存済みフォルダー | `empty`、`populated`、`remove-confirmation` |
| `add-source` | 保存元の追加先選択 | `default` |
| `file-browser` | 再生開始ファイルの選択 | `populated`、`empty`、`loading`、`failure` |
| `dlna-servers` | DLNAサーバー選択・接続 | `populated`、`empty`、`manual-entry`、`connection-error` |
| `player` | 再生画面 | `video-playing`、`video-paused`、`audio-playing`、`ended` |
| `media-info` | メディア情報 | `video`、`audio` |
| `playback-error` | 再生失敗の案内 | `decode` |
| `system-picker` | OSが提供する選択画面 | `default` |
| `permission` | OS・アプリの権限案内 | `default` |

同じ役割には共通IDを使い、表示名や具体的な画面構成は各OSの記録で説明します。プラットフォーム固有の画面は `<platform-id>-<name>` で登録できます。新しい状態は既存IDの意味を変えず追加します。画面の統合・分割がある場合は記録に対応関係を書きます。

未撮影は `pending`、対象外は `not-applicable` として `coverage` に理由を残します。対象外は実装または製品仕様から判断できる場合だけ使用します。「未収録」と「未実装」を同一視しません。

## 4. manifest.json の項目

JSONはUTF-8、`schema_version` は `1`。以下の必須項目を持ちます。

| 項目 | 内容 |
|---|---|
| `schema_version` / `platform` / `capture_id` | 記録形式の版、登録済みOS ID、ディレクトリ名 |
| `captured_at` | タイムゾーン付きISO 8601日時。端末のステータスバー時刻ではない |
| `capture_kind` | `runtime-fixture` / `runtime-device` / `mockup` / `source-reconstruction` |
| `source` | `repository`、完全な `revision`、`production_tree`、`build_variant`、`artifact_sha256`。バイナリがないモック等ではハッシュをnullにして理由を記載 |
| `environment` | 機種・OS・ランタイム・言語・外観・文字倍率・密度・入力・撮影方法等。OS依存値はこのオブジェクトで拡張 |
| `fixture` | サンプルの内容、セットアップ方法、既存データへの影響 |
| `limitations` | 実機未検証、外部機器未接続、テスト経路での状態準備など |
| `screenshots` | 各画像のメタデータの配列（下表） |
| `coverage` | 未撮影・対象外の `scope`、`status`、`reason` の配列 |

画像ごとの必須項目：

| 項目 | 内容 |
|---|---|
| `screen` / `state` / `variant` | 画面・状態・表示条件ID |
| `image` | 記録ディレクトリからの相対PNGパス |
| `title` | 表示内容を識別できる説明 |
| `width_px` / `height_px` | PNGの実寸 |
| `sha256` | 原画像のSHA-256 |
| `preconditions` | 再生状態、メディア、フォーカス等の前提 |
| `steps` | 状態へ到達した操作・撮影ハーネスの準備処理 |
| `observations` | 画像で確認できるUIの事実。動作保証の代用にしない |

条件が共通なら `environment` に置き、特定画像の条件は `environment_overrides` で上書きします。TVのようにフォーカスを比較する記録では、任意の `focus_record` に実測のフォーカス記録JSONへの相対パスを指定できます。各画像の `captured_at` と `environment_overrides.window_focus` にも同じ値を保存します。情報が取れない場合は空欄で推測せず、値をnullにして理由を `limitations` に残します。形式に互換性のない変更を加える場合は `schema_version` を上げます。

## 5. 撮影と追加の手順

1. アプリの参照コミットを固定し、差分とビルドの由来を記録します。共通仕様の案を実装済みとして描きません。
2. 個人データを含まないサンプルを準備します。実データが必要なら収録前に匿名化し、加工した画面はその事実を記録します。
3. 画面ID・状態IDを割り当て、実機／エミュレーター等を記録します。TVはリモコンのフォーカス、デスクトップはウィンドウサイズ、タブレットは分割表示も追加できます。
4. 未加工PNG、`manifest.json`、画像付きの `README.md` を同じ撮影IDへ保存します。リストは中身・空・読込・失敗、再生は動作中・停止・終了・エラーを候補にし、不足は `coverage` に明記します。
5. `catalog.json` と入口の一覧に追加し、次で画像の寸法・ハッシュ・ID・リンクを検査します。

```bash
python3 tools/validate_ui_snapshots.py
```

目視確認では文字切れ、意図しないローディング、回転途中、別ウィンドウの混入、フォーカス、PNGと説明の一致を確認します。自動検査は見た目や操作の正しさを保証しません。
