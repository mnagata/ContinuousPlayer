param([string]$CaptureId = '2026-11-03')
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Drawing
$repoRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../..'))
$snapshots = Join-Path $repoRoot 'ui-snapshots'
$captureRoot = Join-Path $snapshots "windows/$CaptureId"
$manifest = Get-Content -LiteralPath "$captureRoot/manifest.json" -Raw | ConvertFrom-Json
$registry = Get-Content -LiteralPath "$snapshots/screen-registry.json" -Raw | ConvertFrom-Json
$catalog = Get-Content -LiteralPath "$snapshots/catalog.json" -Raw | ConvertFrom-Json
if ($manifest.schema_version -ne 1 -or $manifest.platform -ne 'windows' -or $manifest.capture_id -ne $CaptureId) {
    throw 'Unexpected manifest identity.'
}
$seen = [Collections.Generic.HashSet[string]]::new()
foreach ($shot in $manifest.screenshots) {
    $key = "$($shot.screen)/$($shot.state)/$($shot.variant)"
    if (!$seen.Add($key)) { throw "Duplicate screenshot: $key" }
    if ($shot.state -notin $registry.screens.($shot.screen)) { throw "Unregistered state: $key" }
    foreach ($field in 'title', 'preconditions', 'steps', 'observations', 'captured_at') {
        if (!$shot.$field) { throw "Missing $field for $key" }
    }
    foreach ($pair in @(@($shot.image, $shot.sha256), @($shot.source_image, $shot.source_sha256))) {
        $file = Join-Path $captureRoot $pair[0]
        $resolved = [IO.Path]::GetFullPath($file)
        if (!$resolved.StartsWith($captureRoot + [IO.Path]::DirectorySeparatorChar, [StringComparison]::OrdinalIgnoreCase)) {
            throw "Image escapes capture directory: $file"
        }
        if ((Get-FileHash -LiteralPath $file -Algorithm SHA256).Hash.ToLowerInvariant() -ne $pair[1]) {
            throw "Hash mismatch: $file"
        }
        $image = [Drawing.Image]::FromFile($file)
        try {
            if ($image.Width -ne $shot.width_px -or $image.Height -ne $shot.height_px) {
                throw "Dimension mismatch: $file"
            }
        } finally { $image.Dispose() }
    }
    $bytes = [IO.File]::ReadAllBytes((Join-Path $captureRoot $shot.image))
    if ([Convert]::ToHexString($bytes[0..7]) -ne '89504E470D0A1A0A') { throw "Invalid PNG: $key" }
}
if (@(Get-ChildItem -LiteralPath "$captureRoot/images" -Filter '*.png').Count -ne $manifest.screenshots.Count) {
    throw 'Unlisted or missing PNG files.'
}
if (@(Get-ChildItem -LiteralPath "$captureRoot/source-jpeg" -Filter '*.jpg').Count -ne $manifest.screenshots.Count) {
    throw 'Unlisted or missing JPEG originals.'
}
$record = $catalog.platforms | Where-Object id -eq 'windows'
if ($record.status -ne 'captured' -or !(Test-Path -LiteralPath "$snapshots/$($record.latest)/README.md")) {
    throw 'Windows catalog entry is invalid.'
}
$readme = Get-Content -LiteralPath "$captureRoot/README.md" -Raw
foreach ($match in [regex]::Matches($readme, '\]\(([^)]+)\)')) {
    $target = $match.Groups[1].Value
    if ($target -notmatch '^https?://' -and !(Test-Path -LiteralPath (Join-Path $captureRoot $target))) {
        throw "Broken README link: $target"
    }
}
foreach ($shot in $manifest.screenshots) {
    if (!$readme.Contains("]($($shot.image))")) { throw "Image absent from README: $($shot.image)" }
}
$inventory = Get-Content -LiteralPath "$captureRoot/artifact-files.json" -Raw | ConvertFrom-Json
$records = @($inventory | ForEach-Object { "$($_.path)`0$($_.sha256)`n" }) -join ''
$digest = [Convert]::ToHexString([Security.Cryptography.SHA256]::HashData([Text.Encoding]::UTF8.GetBytes($records))).ToLowerInvariant()
if ($digest -ne $manifest.source.artifact_sha256) { throw 'Artifact inventory digest mismatch.' }
"PASS: $($manifest.screenshots.Count) PNGs, JPEG originals, dimensions, hashes, IDs, README links, catalog, artifact inventory."
