// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {
  CANSparkMax shooterMotor;
  CANSparkMax intakeMotor;
  XboxController driver;

  public RobotContainer() {
    shooterMotor = new CANSparkMax(11, MotorType.kBrushed);
    intakeMotor = new CANSparkMax(12, MotorType.kBrushed);
    driver = new XboxController(0);
    configureBindings();
  }

  private void configureBindings() {
    (new JoystickButton(driver, XboxController.Button.kX.value))
        .onTrue(new InstantCommand(() -> shooterMotor.setVoltage(12)));
    (new JoystickButton(driver, XboxController.Button.kA.value))
        .onTrue(new InstantCommand(() -> intakeMotor.setVoltage(12)));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
