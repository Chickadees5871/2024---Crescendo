package frc.robot.subsystems;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class OperatorInterface extends SubsystemBase {
    public XboxController driveController;
    public XboxController gunnerController;

    public OperatorInterface() {
        driveController = new XboxController(0);
        gunnerController = new XboxController(1);
    }

    public ChassisSpeeds getChassisSpeeds() {

        return new ChassisSpeeds(
                Math.pow(deadzone(driveController.getLeftX(), .2),3),
                Math.pow(deadzone(driveController.getLeftY(), .2),3),
                -Math.pow(deadzone(driveController.getRightX(), .2),3));
    }

    public static double deadzone(double val, double threshold) {
        return Math.abs(val) < threshold ? 0 : val;
    }
}
