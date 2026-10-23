# MiniBank

MiniBank is a Java 17 console banking application demonstrating layered design,
runtime annotation validation, checked banking exceptions, persistence,
collections, and safe concurrent transaction processing.

## Features

- Open savings accounts with validated owner name, email, mobile number, and opening balance.
- Deposit, withdraw, transfer, print account statements, and list saved accounts.
- Process a batch of deposits and withdrawals with a four-thread executor.
- Synchronize account balance updates and order account locks during transfers.
- Append transactions to a log and generate an end-of-day report.
- Save accounts on exit and reload them when the application starts.

## Build and test

From this directory, with JDK 17 and Maven installed:

```sh
mvn test
mvn package
```

The runnable JAR is created at `target/minibank-1.0.0.jar`.

## Run

```sh
java -jar target/minibank-1.0.0.jar
```

The menu options cover account opening, deposits, withdrawals, transfers,
statements, account listing, transaction batches, reports, and saving/exiting.
Account names are limited to 10 characters; mobile numbers and emails are
validated when accounts are opened.

## Runtime files

MiniBank writes runtime data relative to the working directory:

- `data/accounts.dat` — serialized account array, saved on exit.
- `data/logs/transactions.log` — append-only transaction entries.
- `data/end-of-day-report.txt` — generated deposit/withdrawal totals and log-file details.

Keep these runtime files backed up if the account data must be retained.
