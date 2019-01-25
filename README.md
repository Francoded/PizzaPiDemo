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

This repository is composed of three branches, `master`, `android`, and `server`. The master branch contains documentation, the `android` branch contains the Android Studio PizzaPi project, and the `server` branch contains code for the PizzaPi server. Each branch has a README with instructions on how to get started with the PizzaPi Android application and the PizzaPi server. These instructions are catered for UNIX based operating systems that support the recommended software listed above, i.e. MacOS and Linux. 

NOTE: This is not a production-ready system.
