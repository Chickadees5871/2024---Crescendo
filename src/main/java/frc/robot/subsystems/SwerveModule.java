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
        try {
            azimMotor.setSmartCurrentLimit(40);
            driveMotor.setSmartCurrentLimit(40);
            Thread.sleep(2000);
        } catch (Exception e) {
        }

    }

    public void acceptMotion(SwerveModuleState state) {
        double theta = state.angle.getRotations();
        azimController.setReference(theta, ControlType.kPosition);
        driveMotor.set(state.speedMetersPerSecond / maxSpeed);
    }

}
