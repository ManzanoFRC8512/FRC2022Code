__Driver Station README__

This document describes how to use the robot via the driver station.

# Starting the driver station

## Connect to the robot

You'll need to connect to the robot's WiFi adapter to be able to communicate
with it. Note that this will disconnect you from the internet.

Run the powershell script on the desktop _as adminstrator_ called
`wifi-robot.ps1`. This will connect the WiFi adapter to the robot.

If you want to connect back to the internet later, run `wifi-tech-center.ps1` 
_as administrator_.  Just keep in mind that you can't control the robot without
being connected to it over WiFi.

## Deploy code to the robot

The robot will need code to work properly, which needs to be deployed from
LabView.

1. Open `NI LabView 2019`.
2. Open `2020 Robot Project.lvproj`.
3. Open the `Target (roboRIO-8512-FRC.local)` node.
4. Right-click on `Robot Main.vi` and hit `Deploy`.
  1. This step may "fail to deploy target settings" Try again if this
      happens.
  2. This step may open a "Conflict resolution" dialog. Hit `Apply` if
      this happens.

## Start the driver station

You'll need to start the driver station after depolying the code. This is done
by running `FRC Driver Station`

### Teleoperations (ie. controlling the robot with a joystick)

If you want to control the robot with a joystick, go to the driver station.

WARNING: This will effectively enable the robot, so make sure people are
not standing near the robot when you're doing this.

1. Select the steering wheel icon on the left side of the driver station.
2. Press `Teleoperated` on the menu on the left.
3. Press the green `Enable` button below.

This should allow the robot to be operated with the joystick. Don't forget to
turn teleoperations off by pressing the `Disbale` button when you're done,
as leaving teleoperations on is dangerous.

