cargo build --target x86_64-pc-windows-gnu --target x86_64-unknown-linux-gnu --release --workspace
mkdir -p binaries

cp target/x86_64-unknown-linux-gnu/release/runner binaries/
cp target/x86_64-pc-windows-gnu/release/runner.exe binaries/