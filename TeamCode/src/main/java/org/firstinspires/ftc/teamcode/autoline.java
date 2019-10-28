package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;

@Autonomous(name="Drive Mechanism", group ="Concept")
public class autoline extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor myBigMotorFrontLeft,myBigMotorFrontRight,myBigMotorBackLeft,myBigMotorBackRight;
    double tgtPowerFrontLeftMotor = 0;
    double tgtPowerFrontRightMotor = 0;
    double tgtPowerBackLeftMotor = 0;
    double tgtPowerBackRightMotor = 0;
    double rightTgtPower = 0;
    double leftTgtPower = 0;

    @Override
    public void runOpMode() {
        imu = hardwareMap.get(Gyroscope.class, "imu");
        myBigMotorFrontLeft = hardwareMap.get(DcMotor.class, "myBigMotorFrontLeft");
        myBigMotorFrontRight = hardwareMap.get(DcMotor.class, "myBigMotorFrontRight");
        myBigMotorBackLeft = hardwareMap.get(DcMotor.class, "myBigMotorBackLeft");
        myBigMotorBackRight = hardwareMap.get(DcMotor.class, "myBigMotorBackRight");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            rightTgtPower = .1;
            leftTgtPower = 0;

            tgtPowerFrontLeftMotor = leftTgtPower;
            tgtPowerFrontRightMotor = rightTgtPower;
            tgtPowerBackLeftMotor = leftTgtPower;
            tgtPowerBackRightMotor = rightTgtPower;

            myBigMotorFrontLeft.setPower(tgtPowerFrontLeftMotor);
            myBigMotorFrontRight.setPower(tgtPowerFrontRightMotor);
            myBigMotorBackLeft.setPower(tgtPowerBackLeftMotor);
            myBigMotorBackRight.setPower(tgtPowerBackRightMotor);


            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }
}
