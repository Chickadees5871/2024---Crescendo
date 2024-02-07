package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;

import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveModule {
    private SparkPIDController azimController;
    private SparkPIDController driveController;
    private CANSparkMax driveMotor;
    private CANSparkMax azimMotor;
    private CANcoder cancoder;
    double maxSpeed = 3;

    public SwerveModule(CANSparkMax driveMotor, CANSparkMax azimuthMotor, CANcoder cancoder) {

        this.driveMotor = driveMotor;
        this.azimMotor = azimuthMotor;
        azimController = azimMotor.getPIDController();
        driveController = driveMotor.getPIDController();
        this.cancoder = cancoder;

        azimMotor.getEncoder().setPositionConversionFactor(1.0 / 13.3714);
        azimMotor.getEncoder().setPosition(-cancoder.getPosition().getValue());
        azimMotor.setInverted(false);
        azimController.setP(1);

        // azimuthMotor.burnFlash();
        // driveMotor.burnFlash();
        try {
            azimMotor.setSmartCurrentLimit(10);
            driveMotor.setSmartCurrentLimit(10);
            Thread.sleep(2000);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void acceptMotion(SwerveModuleState state) {
        double theta = state.angle.getRotations();
        azimController.setReference(theta, ControlType.kPosition);
        driveMotor.set(state.speedMetersPerSecond / maxSpeed);
        // if(Math.random() >.9 && cancoder.getDeviceID() == 0)
        // System.out.println(azimMotor.getEncoder().getPosition() + "<-- neo
        // cancoder-->" + cancoder.getPosition().getValue());
    }

}
