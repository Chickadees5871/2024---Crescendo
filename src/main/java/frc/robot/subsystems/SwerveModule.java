package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
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
    private RelativeEncoder encoder;
    private DirectSwerveCommand swerveCommand;

    private class DirectSwerveCommand {
        double angle;
        double drive;

        public DirectSwerveCommand(double angle, double drive) {
            this.angle = angle;
            this.drive = drive;
        }
    }

    public SwerveModule(CANSparkMax driveMotor, CANSparkMax azimuthMotor, CANcoder cancoder) {

        this.driveMotor = driveMotor;
        this.azimMotor = azimuthMotor;
        azimController = azimMotor.getPIDController();
        driveController = driveMotor.getPIDController();
        this.cancoder = cancoder;

        // azimMotor.getEncoder().setPositionConversionFactor(1.0 / 13.3714);
        azimMotor.getEncoder().setPositionConversionFactor(Math.PI * 2 / 13.3714);

        azimMotor.getEncoder().setPosition(-cancoder.getPosition().getValue() * Math.PI * 2 + Math.PI /2);
        azimMotor.setInverted(false);
        azimController.setP(1);
        encoder = azimuthMotor.getEncoder();
        swerveCommand = new DirectSwerveCommand(0, 0);
        try {
            azimMotor.setSmartCurrentLimit(40);
            driveMotor.setSmartCurrentLimit(40);
            Thread.sleep(2000);
        } catch (Exception e) {
        }

    }

    public void acceptMotion(SwerveModuleState state) {
        // double theta = state.angle.getRotations();
        double theta = state.angle.getRadians();

        // azimController.setReference(theta, ControlType.kPosition);
        // if(Math.random() > .9)
        // System.out.println(azimMotor.getEncoder().getPosition() + " " +
        // azimMotor.getDeviceId());
        double driveVoltage = state.speedMetersPerSecond / maxSpeed;
        calculateOptimalSetpoint(theta, driveVoltage, encoder.getPosition());
        setReferenceAngle(swerveCommand.angle);
        driveMotor.setVoltage(swerveCommand.drive * 60);
        // if(cancoder.getDeviceID() == 52 && Math.random() > .9){
        //     System.out.println("cancoder = " + (cancoder.getPosition().getValueAsDouble()* 2 * Math.PI) + " encoder: " + encoder.getPosition());
        // }
    }

    public void setReferenceAngle(double referenceAngleRadians) {
        double currentAngleRadians = encoder.getPosition();

        double currentAngleRadiansMod = currentAngleRadians % (2.0 * Math.PI);
        if (currentAngleRadiansMod < 0.0) {
            currentAngleRadiansMod += 2.0 * Math.PI;
        }

        // The reference angle has the range [0, 2pi) but the Neo's encoder can go above
        // that
        double adjustedReferenceAngleRadians = referenceAngleRadians + currentAngleRadians - currentAngleRadiansMod;
        if (referenceAngleRadians - currentAngleRadiansMod > Math.PI) {
            adjustedReferenceAngleRadians -= 2.0 * Math.PI;
        } else if (referenceAngleRadians - currentAngleRadiansMod < -Math.PI) {
            adjustedReferenceAngleRadians += 2.0 * Math.PI;
        }

        azimController.setReference(adjustedReferenceAngleRadians, CANSparkMax.ControlType.kPosition);
    }

    private void calculateOptimalSetpoint(double steerAngle, double driveVoltage, double currentAngle) {

        steerAngle %= (2.0 * Math.PI);
        if (steerAngle < 0.0) {
            steerAngle += 2.0 * Math.PI;
        }

        double difference = steerAngle - currentAngle;
        // Change the target angle so the difference is in the range [-pi, pi) instead
        // of [0, 2pi)
        if (difference >= Math.PI) {
            steerAngle -= 2.0 * Math.PI;
        } else if (difference < -Math.PI) {
            steerAngle += 2.0 * Math.PI;
        }
        difference = steerAngle - currentAngle; // Recalculate difference

        // If the difference is greater than 90 deg or less than -90 deg the drive can
        // be inverted so the total
        // movement of the module is less than 90 deg
        if (difference > Math.PI / 2.0 || difference < -Math.PI / 2.0) {
            // Only need to add 180 deg here because the target angle will be put back into
            // the range [0, 2pi)
            steerAngle += Math.PI;
            driveVoltage *= -1.0;
        }

        // Put the target angle back into the range [0, 2pi)
        steerAngle %= (2.0 * Math.PI);
        if (steerAngle < 0.0) {

            steerAngle += 2.0 * Math.PI;
        }
        swerveCommand.angle = steerAngle;
        swerveCommand.drive = driveVoltage;
        // return swerveCommand;

    }
}
