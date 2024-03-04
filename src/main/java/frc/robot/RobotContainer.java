// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ArmGoToCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.OperatorInterface;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.Hardware.ShooterHardware;

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
    System.out.println(ShooterHardware.getInstance());
  }

  private void configureBindings() {
    Trigger shootAmp = new JoystickButton(oi.gunnerController, XboxController.Button.kB.value);
    //Trigger Button_A = new JoystickButton(oi.driveController, XboxController.Button.kA.value);
    Trigger speakerPos = new JoystickButton(oi.gunnerController, XboxController.Button.kX.value);
    Trigger ampPos = new JoystickButton(oi.gunnerController, XboxController.Button.kY.value);
    Trigger intake = new JoystickButton(oi.gunnerController, XboxController.Button.kLeftBumper.value);
    Trigger shootSpeaker = new JoystickButton(oi.gunnerController, XboxController.Button.kA.value);

  
    intake.onTrue(new ParallelCommandGroup(new InstantCommand(() -> {
      shooter.accept(0);
      intake.accept(-.8);
    }, shooter, intake), new ArmGoToCommand(arm, 2)));

    intake.onFalse(new ParallelCommandGroup(new InstantCommand(() -> {
      shooter.accept(0);
      intake.accept(0);
    }, shooter, intake), new ArmGoToCommand(arm, 2)));


    //Since we are using a limit switch, running the intake backwards is not necessary.
    //Spins up the shooter at 28 degrees then runs the intake to push note into shooter
    shootSpeaker.onTrue(new SequentialCommandGroup(
      new InstantCommand(() -> {
          //shooter.accept(0);
          //intake.accept(2);
        }, shooter, intake),
        //new WaitCommand(.05),
      new InstantCommand(() -> {
          shooter.accept(-12);
                    intake.accept(0);
        }, shooter,intake),
        new ArmGoToCommand(arm, 28),
        new WaitCommand(2),
        new InstantCommand(() -> {
          intake.accept(-12);
        }, intake)));


    shootSpeaker.onFalse(new ParallelCommandGroup(new InstantCommand(() -> {
      shooter.accept(0);
      intake.accept(0);
    }, shooter, intake), new ArmGoToCommand(arm, 2)));

    ampPos.onTrue(new ArmGoToCommand(arm, 90));
    speakerPos.onTrue(new ArmGoToCommand(arm, 20));

  //Spins up shooter at 90 degrees then runs the intake to push note into shooter
  shootAmp.onTrue(new SequentialCommandGroup(
    new InstantCommand(() -> {
      shooter.accept(-12);
      intake.accept(0);
    }, shooter, intake),
    new ArmGoToCommand(arm, 90),
    new WaitCommand(1),
    new InstantCommand(() -> {
      intake.accept(-12);
    }, intake)
  ));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}

