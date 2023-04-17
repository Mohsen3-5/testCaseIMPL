# Overlay Service Implementation

This Android project implements a service that creates and manages an overlay view on top of other applications. The purpose of this service is to display a custom view over other applications.

# How it works

When the service is created, a notification is displayed to the user indicating that the service is running in the background. The overlay view is then created by inflating a layout resource file and adding it to the WindowManager. The overlay view is a TextView with the name of the application displayed.

When the service is destroyed, the overlay view is removed from the WindowManager.

# Dependencies
This project uses Dagger Hilt for dependency injection.

# Requirements
This service requires Android API level 30 or higher.
