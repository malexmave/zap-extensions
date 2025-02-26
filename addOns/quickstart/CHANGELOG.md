# Changelog
All notable changes to this add-on will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## Unreleased
### Changed
- Spider checkboxes in Automated Scan will be disabled when scan is running. (Issue 7072)
- Use Network add-on to obtain main proxy address/port.
- Maintenance changes.
- Use Spider add-on (Issue 3113).

### Fixed
- Accept any 2xx result code instead of just 200.

## [33] - 2021-12-13
### Changed
- Update minimum ZAP version to 2.11.1.
- Browser Launch/Manual Explore will now display a warning dialog if the selected browser executable cannot be found (Issue 6963).

## [32] - 2021-12-06
### Changed
- Use the Network add-on to export the Root CA certificate.

## [31] - 2021-11-23
### Changed
- Use callhome add-on for getting the news

## [30] - 2021-10-06
### Added
- Automation link

### Changed
- Now using 2.10 logging infrastructure (Log4j 2.x).
- Maintenance changes.
- Generate quickout reports using the reports add-on instead of the core
- Update minimum ZAP version to 2.11.0.
- Disable browser launch in containers unless override option enabled
- Video link to point to ZAP website

## [29] - 2020-12-15
### Changed
- Update minimum ZAP version to 2.10.0.
- Maintenance changes.
- Use appropriate colour in dark mode (Issue 5542).

### Fixed
- Use AJAX Spider options in Automated Scan (Issue 5981).

## [28] - 2020-02-04
### Added
- Warning when HUD is enabled only in scope


## [27] - 2020-01-17
### Added
- Add info and repo URLs.
- Added online link to ZAP in Ten videos

### Changed
- Maintenance changes.
- Link to newer Getting Started Guide.
- Improve permissions and space handling when saving.
- Updated for the new site

## [26] - 2019-06-07

- Improve outgoing proxy failure error message (Issue 5304).
- Introduce News panel and use default quick start page 
- Dont make news request if -silent option used
- Allow to use headless browsers in automated scans, use Firefox headless by default (Issue 3866).
- Depend on newer version of Selenium add-on.

## 25 - 2018-12-11

- Inform when quick attack is disabled by the current mode (Issue 5069).
- Notify when quick attack starts.
- Include expected status code in the error message.
- Removed PnH code (Issue 5136).

## 24 - 2018-08-03

- Enable the extensions for all DB types.
- Use default user agent when creating seeds for the spider.

## 23 - 2018-01-19

- Update for 2.7.0.
- Stop the quick scan if the session is changed.
- Added toolbar button to launch the latest browser chosen.

## 22 - 2017-11-27

- Code changes for Java 9 (Issue 2602).
- Updated to use new icon.
- Updated for the new default browser launch URL.

## 21 - 2017-08-18

- Add option to launch a browser via selenium (v20).
- Fix to the default launch page url for 2.6.0.

## 20 - 2017-08-18

- Add option to launch a browser via selenium.

## 19 - 2017-04-06

- Validate the provided URL as a request-uri.

## 18 - 2016-06-02

- Issue 1271: Quickstart PnH panel components should mirror the enabled state of the API (i.e.: If API disabled, disable PnH components).

## 17 - 2015-12-04

- Add progress indications for quick scan launched from the command line (Issue 1891)
- Improve Quick Attack error messages (Issue 2032)

## 16 - 2015-07-30

- Include API key in PNH URL (Issue 1714).

## 15 - 2015-04-13

- Generate the report (arg -quickout) even if view is initialised (Issue 1281).
- Updated for ZAP 2.4

## 14 - 2014-04-10

- Changed help file structure to support internationalisation (Issue 981).
- Added content-type to help pages (Issue 1080).
- Updated add-on dir structure (Issue 1113).

## 13 - 2013-09-11

- Updated for 2.2.0.

## 12 - 2013-06-24

- Link to mitmconf add-on URL if present.

## 11 - 2013-05-27

- Updated language files.

## 10 - 2013-04-18

- Updated for ZAP 2.1.0

## 8 - 2013-02-11

- Added scroll pane for small resolutions and dark icon when used in dev

## 7 - 2013-02-05

- Added support for modes

## 6 - 2013-01-28

- Promoted to release status, re-added missing help files

## 4 - 2013-01-21

- Added context sensitive help

## 3 - 2013-01-17

- Updated to support new addon format

## 2 - 2012-12-06



[33]: https://github.com/zaproxy/zap-extensions/releases/quickstart-v33
[32]: https://github.com/zaproxy/zap-extensions/releases/quickstart-v32
[31]: https://github.com/zaproxy/zap-extensions/releases/quickstart-v31
[30]: https://github.com/zaproxy/zap-extensions/releases/quickstart-v30
[29]: https://github.com/zaproxy/zap-extensions/releases/quickstart-v29
[28]: https://github.com/zaproxy/zap-extensions/releases/quickstart-v28
[27]: https://github.com/zaproxy/zap-extensions/releases/quickstart-v27
[26]: https://github.com/zaproxy/zap-extensions/releases/quickstart-v26
