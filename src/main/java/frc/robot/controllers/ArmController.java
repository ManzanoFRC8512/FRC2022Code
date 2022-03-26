package frc.robot.controllers;

final class ArmController implements Controller {
    @Override
    public void robotInit() {
        arm.setInverted(false);
        arm.setIdleMode(IdleMode.kBrake);
        arm.burnFlash();
    }

    @Override
    public void autonomousInit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void autonomousPeriodic() {
        // TODO Auto-generated method stub

    }

    @Override
    public void teleopInit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void teleopPeriodic() {
        // TODO Auto-generated method stub

    }

    @Override
    public void disabledInit() {
        // TODO Auto-generated method stub

    }

}
