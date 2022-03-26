package frc.robot.controllers;

public interface Controller {
    void robotInit();
    void autonomousInit();
    void autonomousPeriodic();
    void teleopInit();
    void teleopPeriodic();
    void disabledInit();
}
