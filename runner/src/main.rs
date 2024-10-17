use chrono::Local;
use clap::Parser;
use colored::Colorize;
use notify::{Event, Watcher};
use std::{path::PathBuf, process::Command, sync::mpsc, thread};

#[derive(Parser, Debug, Clone)]
struct Arguments {
    #[arg(short, long, help = "Whether to watch the source directory")]
    pub watch: bool,

    #[arg(
        short,
        long,
        help = "Java class to execute (i.e. testers.JottTokenizerTester)"
    )]
    pub target: String,

    #[arg(long, help = "Override for source path", default_value = "src")]
    pub source: PathBuf,

    #[arg(long, help = "Override for build path", default_value = "build")]
    pub build: PathBuf,
}

pub fn execute(target: String, source: PathBuf, build: PathBuf) {
    println!(
        "{} {} {} {} =====",
        "Executing".green().bold(),
        target,
        "@".bold(),
        Local::now().format("%Y-%m-%d :: %H:%M:%S")
    );
    if cfg!(target_os = "windows") {
        let build_result = Command::new("cmd")
            .args([
                "/C",
                format!(
                    "javac -d {} $(find {} -name \"*.java\")",
                    build.clone().into_os_string().to_str().unwrap(),
                    source.clone().into_os_string().to_str().unwrap()
                )
                .as_str(),
            ])
            .output()
            .expect("Failed to start javac");
        if !build_result.status.success() {
            println!(
                "{} Failed to build with status code {}:\n\nstdout:\n{}\n\nstderr:\n{}",
                "Failure:".red().bold(),
                build_result.status.code().or(Some(-1)).unwrap(),
                String::from_utf8(build_result.stdout).expect("javac output was not UTF-8"),
                String::from_utf8(build_result.stderr).expect("javac output was not UTF-8")
            );
            return ();
        }

        let exec_result = Command::new("cmd")
            .args([
                "/C",
                format!(
                    "java -cp {} {}",
                    build.clone().into_os_string().to_str().unwrap(),
                    target
                )
                .as_str(),
            ])
            .output()
            .expect("Failed to execute target class");
        if !exec_result.status.success() {
            println!(
                "{} Failed to execute with status code {}:\n\nstdout:\n{}\n\nstderr:\n{}",
                "Failure:".red().bold(),
                exec_result.status.code().or(Some(-1)).unwrap(),
                String::from_utf8(exec_result.stdout).expect("javac output was not UTF-8"),
                String::from_utf8(exec_result.stderr).expect("javac output was not UTF-8")
            );
            return ();
        }

        println!(
            "{} Code returned a successful error code & the following output:\n\nstdout:\n{}\n\nstderr:\n{}",
            "Success:".green().bold(),
            String::from_utf8(exec_result.stdout).expect("javac output was not UTF-8"),
            String::from_utf8(exec_result.stderr).expect("javac output was not UTF-8")
        );
    } else {
        let build_result = Command::new("sh")
            .args([
                "-c",
                format!(
                    "javac -d {} $(find {} -name \"*.java\")",
                    build.clone().into_os_string().to_str().unwrap(),
                    source.clone().into_os_string().to_str().unwrap()
                )
                .as_str(),
            ])
            .output()
            .expect("Failed to start javac");
        if !build_result.status.success() {
            println!(
                "{} Failed to build with status code {}:\n\nstdout:\n{}\n\nstderr:\n{}",
                "Failure:".red().bold(),
                build_result.status.code().or(Some(-1)).unwrap(),
                String::from_utf8(build_result.stdout).expect("javac output was not UTF-8"),
                String::from_utf8(build_result.stderr).expect("javac output was not UTF-8")
            );
            return ();
        }

        let exec_result = Command::new("sh")
            .args([
                "-c",
                format!(
                    "java -cp {} {}",
                    build.clone().into_os_string().to_str().unwrap(),
                    target
                )
                .as_str(),
            ])
            .output()
            .expect("Failed to execute target class");
        if !exec_result.status.success() {
            println!(
                "{} Failed to execute with status code {}:\n\nstdout:\n{}\n\nstderr:\n{}",
                "Failure:".red().bold(),
                exec_result.status.code().or(Some(-1)).unwrap(),
                String::from_utf8(exec_result.stdout).expect("javac output was not UTF-8"),
                String::from_utf8(exec_result.stderr).expect("javac output was not UTF-8")
            );
            return ();
        }
        println!(
            "{} Code returned a successful error code & the following output:\n\nstdout:\n{}\n\nstderr:\n{}",
            "Success:".green().bold(),
            String::from_utf8(exec_result.stdout).expect("javac output was not UTF-8"),
            String::from_utf8(exec_result.stderr).expect("javac output was not UTF-8")
        );
    }
}

fn main() {
    let args = Arguments::parse();
    if args.watch {
        /*let mut watcher = notify::recommended_watcher(move |event: notify::Result<Event>| {
            match event {
                Ok(ev) => {
                    println!("{} {:?}. Rebuilding...", "Detected changed files:".bold());
                    execute(args.target, args.source, args.build);
                },
                Err(ev) => println!("{} {:?}", "Watcher Error:".red().bold(), ev)
            }
        }).expect("Failed to setup watcher.");*/

        let target = args.target.clone();
        let source = args.source.clone();
        let build = args.build.clone();

        let (tx, rx) = mpsc::channel::<Vec<PathBuf>>();

        let mut watcher = notify::recommended_watcher(move |res: notify::Result<Event>| match res {
            Ok(event) => {
                tx.send(event.paths).expect("Failed to send update from watcher")
            }
            Err(e) => println!("watch error: {:?}", e),
        })
        .expect("FAIL");

        let _handle = thread::spawn(move || {
            for event in &rx {
                println!(
                    "{} {event:?}. Rebuilding...",
                    "Detected changed files:".bold()
                );
                execute(target.clone(), source.clone(), build.clone());
            }
            ()
        });

        watcher
            .watch(&args.source, notify::RecursiveMode::Recursive)
            .expect("Failed to start watcher.");

        loop {}

    } else {
        execute(args.target, args.source, args.build);
    }
}
