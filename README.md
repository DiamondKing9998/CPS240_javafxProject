# 2025 RoboKings Scouting App

FRC Team 6753's scouting app for the 2025 FRC game, **REEFSCAPE**.

Scouts enter what one robot does during a match. When they finish, the app turns that data into a QR code. The scouting lead scans the code to add the match to a spreadsheet.

The app is a Java 25 desktop app built with **JavaFX 21** and **Maven**. It uses **ZXing** to make the QR codes.

---

## Development setup

### What you need

| Tool | Version | Why |
|------|---------|-----|
| JDK (Java Development Kit) | **25** | Compiles and runs the app |
| Git | any | Clones the repo |
| IDE (optional) | IntelliJ IDEA Community (recommended) or VS Code | Editing and running |
| Scene Builder (optional) | any | Visual editor for the `.fxml` screen layouts |

You **do not** need to install Maven or JavaFX yourself. The repo includes the Maven Wrapper (`mvnw` / `mvnw.cmd`), which downloads the right Maven version. Maven then downloads JavaFX and ZXing on the first build.

---

### Windows

1. **Install JDK 25.**
   - Download **Eclipse Temurin 25** from https://adoptium.net and run the installer.
   - In the installer, turn on **"Set JAVA_HOME variable"** and **"Add to PATH"**.
   - Or use winget in PowerShell:
     ```powershell
     winget install EclipseAdoptium.Temurin.25.JDK
     ```
2. **Install Git** from https://git-scm.com/download/win (the default options are fine), or:
   ```powershell
   winget install Git.Git
   ```
3. **Open a new terminal** so it picks up the PATH changes. Then check both installs:
   ```powershell
   java -version
   git --version
   ```
4. **Clone the repo:**
   ```powershell
   git clone https://github.com/DiamondKing9998/CSP240_javafxProject.git
   cd CSP240_javafxProject
   ```
5. **Create the team list.** See [Runtime files](#runtime-files-required) below.
6. **Run the app** from the repo root:
   ```powershell
   .\mvnw.cmd clean javafx:run
   ```
   The first run takes a minute or two while Maven downloads its dependencies.

---

### macOS

1. **Install Homebrew** if you don't have it (see https://brew.sh):
   ```bash
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   ```
2. **Install JDK 25 and Git:**
   ```bash
   brew install --cask temurin@25
   brew install git
   ```
   (You can also get the macOS `.pkg` installer from https://adoptium.net. Pick **aarch64** for Apple Silicon M-series Macs and **x64** for Intel Macs.)
3. **Check both installs:**
   ```bash
   java -version
   git --version
   ```
   If `java -version` doesn't show 25, point `JAVA_HOME` at the new JDK. To make this permanent, add the line to `~/.zshrc`:
   ```bash
   export JAVA_HOME=$(/usr/libexec/java_home -v 25)
   ```
4. **Clone the repo:**
   ```bash
   git clone https://github.com/DiamondKing9998/CSP240_javafxProject.git
   cd CSP240_javafxProject
   ```
5. **Make the Maven Wrapper executable** (Git doesn't always keep this setting):
   ```bash
   chmod +x mvnw
   ```
6. **Create the team list.** See [Runtime files](#runtime-files-required) below.
7. **Run the app** from the repo root:
   ```bash
   ./mvnw clean javafx:run
   ```

---

### Runtime files (required)

The app reads and writes files in a folder called `robokingsscouting/`. It finds this folder **relative to the directory you launch the app from**, so always run the app from the repo root. (In an IDE, set the run configuration's working directory to the repo root.)

```
robokingsscouting/
├── eventTeams.txt       # YOU create this: one team number per line
└── qr_codes/            # created automatically; generated QR PNGs go here
```

`eventTeams.txt` is **not** in the repo, because it changes for every event. Create it before you run the app. Otherwise the team dropdown on the first screen will be empty:

```
6753
254
1678
```

**Font:** the screens use *Berlin Sans FB*. The Regular, Bold and Demi Bold faces are bundled in the app's resources (`src/main/resources/.../fonts/`) and load automatically at startup, so you don't need to install anything.

---

### Using an IDE

**IntelliJ IDEA (recommended):**
1. Choose **File → Open** and pick the repo folder. IntelliJ detects `pom.xml` and imports it as a Maven project.
2. Under **File → Project Structure → Project**, set the SDK to your JDK 25.
3. Open the **Maven** tool window and run **Plugins → javafx → javafx:run**.
   - Or run `PrimaryWindow`'s `main` method directly. If you do, check that the working directory is the repo root.

**VS Code:** install the **Extension Pack for Java**, open the folder, then run `./mvnw clean javafx:run` (macOS) or `.\mvnw.cmd clean javafx:run` (Windows) in the built-in terminal.

If you use VS Code's Run button and get `Unsupported major.minor version 69.0`, VS Code is running the app on an older JDK. Point it at JDK 25: set `java.configuration.runtimes` (Red Hat Java extension) or `jdk.jdkhome` (Oracle Java extension) in `.vscode/settings.json`. Then run **Java: Clean Java Language Server Workspace** from the Command Palette and reload.

**Editing screens:** the UI layouts live in `src/main/resources/.../fxml/*.fxml`. You can edit them by hand or open them in [Scene Builder](https://gluonhq.com/products/scene-builder/).

---

### Useful commands

Use `./mvnw` on macOS and `.\mvnw.cmd` on Windows.

| Command | What it does |
|---------|--------------|
| `mvnw clean javafx:run` | Build and run the app |
| `mvnw clean compile` | Compile only, to check for errors |
| `mvnw clean package` | Build a jar in `target/` |

---

## How the code works

All the source code is in `src/main/java/com/cbarobokings/robokings2025scouting/`. The screen layouts are in `src/main/resources/com/cbarobokings/robokings2025scouting/fxml/`.

### Screen flow

The app is one window that moves through a series of screens. Each screen has an FXML layout file and a matching controller class:

```
Prematch → Autonomous → Teleop → Endgame → DataConfirmation → QRCode
   ↑                                                            │
   └──────────────────── "Scout Next Match" ────────────────────┘
```

| Screen | Controller | What the scout enters or sees |
|--------|------------|-------------------------------|
| `Prematch.fxml` | `PrematchController` | Scout name, team (from `eventTeams.txt`), match number, starting position, preloaded coral |
| `Autonomous.fxml` | `AutonomousController` | Algae and coral collected, scored, dropped, and netted during auto, plus whether the robot left the starting zone |
| `Teleop.fxml` | `TeleopController` | The same game-piece counts for teleop |
| `Endgame.fxml` | `EndgameController` | Endgame position, whether the robot played defense, total alliance points, comments |
| `DataConfirmation.fxml` | `DataConfirmationController` | A review table of everything entered. The button on this screen generates the QR code. |
| `QRCode.fxml` | `QRCodeController` | Shows the QR code to scan. Has buttons to start the next match or clear old QR files. |

Back and Next buttons on each screen call `PrimaryWindow.switchScene("SomeScreen.fxml")`, which loads the new layout into the same window.

### Key classes

- **`PrimaryWindow`** is the JavaFX `Application` and the app's entry point. It sets up the window, creates the `robokingsscouting/` folder, and shows the Prematch screen first. It also has shared helpers:
  - `switchScene(...)` changes screens.
  - `generateQRcode(...)` writes a QR PNG using ZXing.
  - `loadChoicesFromFile(...)` reads `eventTeams.txt`.
  - `clearQRCodesDirectory()` deletes old QR images.
- **`SceneDataStore`** is a singleton key/value map that holds the current match's data. A new controller is created every time a screen loads, so each controller saves its fields here as the scout types (through listeners) and reads them back when the screen opens again. That way, going Back doesn't lose anything. `clearData()` empties the store before the next match.
- **`NumericTextFieldFilter`** limits a text field to digits. The match number and count fields use it.
- **`FontLoader`** loads the bundled fonts from `resources/.../fonts/` into JavaFX. `PrimaryWindow.start()` calls it before the first screen loads, so FXML can refer to them by family name (for example `"Berlin Sans FB"`). To add another font, drop the `.ttf` file in that folder and add its name to `FONT_FILES`.
- **`ExecutableApp`** is a plain `main` wrapper used as the jar's `Main-Class`. JavaFX apps can't launch directly from a fat jar whose main class extends `Application`, so the jar starts through this class instead.

### QR code output

When the scout clicks **View QR Code**, `DataConfirmationController` joins every value with **tab characters**, in a fixed order: prematch → auto → teleop → endgame. ZXing encodes that string as a QR image and saves it to:

```
robokingsscouting/qr_codes/qrcode_M<match>_<position>.png
```

Scanning the code with a scanner that types like a keyboard pastes one tab-separated row, so each value lands in its own spreadsheet column. **If you add, remove, or reorder a field**, update the `String.join(...)` call in `DataConfirmationController.viewQRCodeButtonClick` and the spreadsheet's column headers together.

### Adding a new field (checklist)

1. Add the control to the screen's `.fxml` file and give it an `fx:id`.
2. Add a matching `@FXML` field in that screen's controller. In `initialize()`, restore its value from `SceneDataStore` and add a listener that saves changes back to the store.
3. Read the value in `DataConfirmationController`. Add it to the review table and to the QR `String.join(...)`.
4. Add the matching column to the scouting spreadsheet.

---

## License

See [LICENSE](LICENSE).
