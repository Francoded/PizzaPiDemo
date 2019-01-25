## PizzaPi
PizzaPi is a web and Android application that provides its users a convenient way of discovering recipes based on some list of ingredients. This repository is meant to revive PizzaPi for demonstration purposes. The demo provided by this repository lacks behind the original PizzaPi demo in the sense that it does not make use of real recipe data. The original PizzaPi requested temporary Academic access to recipe and ingredient data provided by the <a href="https://spoonacular.com/food-api">FoodAPI</a>.

Overall, the sample illustrates a working full stack Android and web application.

### Recommended Setup

I recommend running PizzaPi in an Android Studio virtual device and running the PizzaPi server on the same machine, i.e. on `localhost`. The PizzaPi server dependencies are easily managed using Anaconda and the provided _environment.yml_ file.

#### Software
* Android Studio
* Anaconda

#### Requirements
* A database compatible with SQLAlchemy and follows the schema described in _schema.pdf_
* The machine running the server should be accessible by the Android application

### Getting Started

These instructions are catered for UNIX based operating systems that support the recommended software listed below, i.e. MacOS and Linux.

#### Android

1. Run Android Studio and create a new project from this GitHub repo
2. Change branch to 'Android'
3. Close project and import the project once again to regenerate important files
4. Update the `serverIP` string in the _strings.xml_ file of the `app` module
5. Run PizzaPi on a physical or virtual device

Example Virtual Device Configuration:
* Device: Nexus 5 4.95” 1080x1920
* Target: API 24
* CPU/ABI: Google APIs Intel Atom (x86_64)
* Hardware Keyboard Present is recommended
* Skin:Skinwithdynamichardwarecontrols
* Adjust memory and processor settings accordingly

#### Server

NOTE: The server is set up to run a Flask development server on TCP port 5000 and should not be used in production.

1. Use Anaconda to create an environment from the provided _environment.yml_ file
2. Activate the newly created Anaconda environment
3. Update the database URI to point to a compatible database 
3. Run the server using `python run.py`
