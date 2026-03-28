# AWS Bedrock Configuration
$env:AWS_REGION="eu-central-1"
$env:CLAUDE_CODE_USE_BEDROCK="1"

$env:ANTHROPIC_DEFAULT_OPUS_MODEL="eu.anthropic.claude-opus-4-6-v1[1m]"
$env:ANTHROPIC_DEFAULT_SONNET_MODEL="eu.anthropic.claude-sonnet-4-6[1m]"
$env:ANTHROPIC_DEFAULT_HAIKU_MODEL="eu.anthropic.claude-haiku-4-5-20251001-v1:0"

$env:ANTHROPIC_MODEL="opusplan"

# Verknüpfung zum AWS CLI Profil
$env:AWS_PROFILE="bedrock-dev"

# Optional: Performance Settings
#$env:CLAUDE_CODE_MAX_OUTPUT_TOKENS="4096"
$env:MAX_THINKING_TOKENS="4096"
$env:BASH_DEFAULT_TIMEOUT_MS="300000"      # 5 Min für lange Operationen

# Optional: Telemetrie
$env:DISABLE_TELEMETRY="1"                 # Telemetrie deaktivieren

# Optional: Subagent Model
$env:CLAUDE_CODE_SUBAGENT_MODEL="eu.anthropic.claude-sonnet-4-6"    # Schnelleres Subagent-Modell

claude
