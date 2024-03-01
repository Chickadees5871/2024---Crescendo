// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ArmGoToCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.OperatorInterface;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;

public class RobotContainer {
  public SwerveDrive swerveDrive;
  public OperatorInterface oi;
  public final DriveCommand driveCommand;
  public final Arm arm;
  public final Shooter shooter;
  public final Intake intake;

  public RobotContainer() {

    swerveDrive = new SwerveDrive();
    oi = new OperatorInterface();
    driveCommand = new DriveCommand(oi, swerveDrive);
    arm = Arm.getInstance();
    shooter = Shooter.getInstance();
    intake = Intake.getInstance();
    configureBindings();
  }

  private void configureBindings() {
    Trigger Button_Y = new JoystickButton(oi.driveController, XboxController.Button.kY.value);
    Trigger Button_A = new JoystickButton(oi.driveController, XboxController.Button.kA.value);
    Trigger Button_X = new JoystickButton(oi.driveController, XboxController.Button.kX.value);
    Trigger Button_B = new JoystickButton(oi.driveController, XboxController.Button.kB.value);

    Button_A.onTrue(new InstantCommand(() -> {
      shooter.accept(1);
    }, shooter));
    Button_B.onTrue(new InstantCommand(() -> {
      intake.accept(.2);
    }, intake));
    Button_Y.onTrue(new ArmGoToCommand(arm, 90));
    Button_X.onTrue(new ArmGoToCommand(arm, 20));

  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
