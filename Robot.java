package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Robot {
    DriveController driveController;
    IMU imu;
    Telemetry telemetry;
    HardwareMap hardwareMap;
    OpMode opMode;

    public Robot (OpMode opMode, boolean isAuto) {
        this.hardwareMap = opMode.hardwareMap;
        this.telemetry = opMode.telemetry;
        this.opMode = opMode;

        // Default name in Hardware Map for REV Control Hub internal IMU is "imu"
        imu = hardwareMap.get(IMU.class, "imu");
        initIMU();

        driveController = new DriveController(this);
    }

    public void initIMU() {
        // Updated to match: Logo facing BACKWARD, USB ports facing UP
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.UP;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
    }

    public void resetYaw() {
        imu.resetYaw();
    }

    public Angle getRobotHeading() {
        double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        return new Angle(-heading, Angle.AngleType.NEG_180_TO_180_HEADING);
    }

    public void wait(int millis, LinearOpMode linearOpMode) {
        long startTime = System.currentTimeMillis();
        while (millis > System.currentTimeMillis() - startTime && linearOpMode.opModeIsActive()) {}
    }
}