package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="Drive Mechanism", group ="Concept")
public class myBigTest extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor myBigMotorFrontLeft,myBigMotorFrontRight,myBigMotorBackLeft,myBigMotorBackRight;
    double tgtPowerFrontLeftMotor = 0;
    double tgtPowerFrontRightMotor = 0;
    double tgtPowerBackLeftMotor = 0;
    double tgtPowerBackRightMotor = 0;
    double rightTgtPower = 0;
    double leftTgtPower = 0;
    double direction = 0;
    double servoPowerArmBase = .25;
    double servoPowerArmMid = 0;
    double servoPowerArmClaw = .1;
    //We couldn't find a digital touch
    //private DigitalChannel digitalTouch;
    //We couldn't find a sensorColorRange
    //private DistanceSensor sensorColorRange;

    private CRServo myBigServoArmBase;
    private CRServo myBigServoArmMid;
    private CRServo myBigServoClaw;

    boolean bPressed = false;
    int bPressedCount = 0;


    @Override
    public void runOpMode() {
        imu = hardwareMap.get(Gyroscope.class, "imu");
        myBigMotorFrontLeft = hardwareMap.get(DcMotor.class, "myBigMotorFrontLeft");
        myBigMotorFrontRight = hardwareMap.get(DcMotor.class, "myBigMotorFrontRight");
        myBigMotorBackLeft = hardwareMap.get(DcMotor.class, "myBigMotorBackLeft");
        myBigMotorBackRight = hardwareMap.get(DcMotor.class, "myBigMotorBackRight");
        //digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
        // sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
        myBigServoArmBase = hardwareMap.get(CRServo.class, "myBigServoArmBase");
        myBigServoArmMid = hardwareMap.get(CRServo.class, "myBigServoArmMid");
        myBigServoClaw = hardwareMap.get(CRServo.class, "myBigServoClaw");
        myBigServoArmBase.setDirection(CRServo.Direction.REVERSE);
        myBigServoArmBase.setPower(servoPowerArmBase);
        myBigServoArmMid.setPower(servoPowerArmMid);
        myBigServoClaw.setPower(servoPowerArmClaw);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //tgtPower = this.gamepad1.right_trigger - this.gamepad1.left_trigger;
            //direction = this.gamepad1.left_stick_x * .5;

            //tgtPowerFrontLeftMotor = tgtPower;
            //tgtPowerFrontRightMotor = tgtPower;
            //tgtPowerBackLeftMotor = tgtPower;
            //tgtPowerBackRightMotor = tgtPower;

            rightTgtPower = -this.gamepad1.right_stick_y;
            leftTgtPower = this.gamepad1.left_stick_y;

            tgtPowerFrontLeftMotor = leftTgtPower;
            tgtPowerFrontRightMotor = rightTgtPower;
            tgtPowerBackLeftMotor = leftTgtPower;
            tgtPowerBackRightMotor = rightTgtPower;


            /*if(direction != 0){
                if(tgtPower == 0){
                    //Make Grease Lightning Pivot at full speed
                    direction = this.gamepad1.left_stick_x;
                    if (direction <= 0) {
                        tgtPowerFrontLeftMotor = 1 * direction;
                        tgtPowerBackLeftMotor = 1 * direction;
                        tgtPowerFrontRightMotor = 1 * -direction;
                        tgtPowerBackRightMotor = 1 * -direction;
                    } else if (direction >= 0) {
                        tgtPowerFrontRightMotor = 1 * -direction;
                        tgtPowerBackRightMotor = 1 * -direction;
                        tgtPowerFrontLeftMotor = 1 * direction;
                        tgtPowerBackLeftMotor = 1 * direction;
                    }
                } else if(tgtPower > 0) {

                    direction = this.gamepad1.left_stick_x * .5;

                    if (direction <= 0) {
                        tgtPowerFrontLeftMotor = tgtPower * -direction;
                        tgtPowerBackLeftMotor = tgtPower * -direction;
                    } else if (direction >= 0) {
                        tgtPowerFrontRightMotor = tgtPower * direction;
                        tgtPowerBackRightMotor = tgtPower * direction;
                    }
                }
                else if(tgtPower < 0){

                    direction = this.gamepad1.left_stick_x * .5;

                    if(direction <= 0){
                        tgtPowerFrontLeftMotor = tgtPower * -direction;
                        tgtPowerBackLeftMotor = tgtPower * -direction;
                    } else if(direction >= 0){
                        tgtPowerFrontRightMotor = tgtPower * direction;
                        tgtPowerBackRightMotor = tgtPower * direction;
                    }
                }
            }*/

            myBigMotorFrontLeft.setPower(tgtPowerFrontLeftMotor);
            myBigMotorFrontRight.setPower(tgtPowerFrontRightMotor);
            myBigMotorBackLeft.setPower(tgtPowerBackLeftMotor);
            myBigMotorBackRight.setPower(tgtPowerBackRightMotor);

            //Drop Claw onto stone
            if(this.gamepad1.b) {
                //myBigServoClaw.setDirection(CRServo.Direction.FORWARD);
                myBigServoClaw.setPower(.3);
                sleep(200);
                //myBigServoArmMid.setDirection(CRServo.Direction.FORWARD);
                myBigServoArmMid.setPower(.6);
                sleep(200);

                myBigServoArmBase.setPower(.7);
                //sleep(500);


            }

            //Grab stone and lift
            if(this.gamepad1.a) {
                //myBigServoClaw.setDirection(CRServo.Direction.FORWARD);
                myBigServoClaw.setPower(1);
                sleep(500);
                //myBigServoArmBase.setDirection(CRServo.Direction.FORWARD);
                myBigServoArmBase.setPower(.7);

                //sleep(200);
                //myBigServoArmMid.setDirection(CRServo.Direction.REVERSE);
                myBigServoArmMid.setPower(-.4);

                //New Code to test
                //myBigServoArmMid.setPower(.7);
                //myBigServoArmBase.setPower(-.7);
            }

            //Crane Return to Start Position
            if(this.gamepad1.y){
                //myBigServoClaw.setDirection(CRServo.Direction.FORWARD);
                myBigServoClaw.setPower(1);
                sleep(500);
                //myBigServoArmBase.setDirection(CRServo.Direction.FORWARD);
                myBigServoArmMid.setPower(0);
                sleep(200);
                //myBigServoArmMid.setDirection(CRServo.Direction.REVERSE);
                myBigServoArmBase.setPower(.25);
                sleep(200);
                myBigServoClaw.setPower(.1);

            }

            //Servo Code
            //myBigServoClaw.setPower(this.gamepad1.right_trigger);
            //myBigServoArmMid.setPower(servoPowerArmMid);

            //telemetry.addData("Front Left Motor Power", myBigMotorFrontLeft.getPower());
            //telemetry.addData("Front Right Motor Power", myBigMotorFrontRight.getPower());
            //telemetry.addData("Back Left Motor Power", myBigMotorBackLeft.getPower());
            //telemetry.addData("Back Right Motor Power", myBigMotorBackRight.getPower());
            telemetry.addData("Direction", myBigServoArmBase.getDirection());
            telemetry.addData("Arm Base", myBigServoArmBase.getPower());
            telemetry.addData("Direction", myBigServoArmMid.getDirection());
            telemetry.addData("Arm Mid", myBigServoArmMid.getPower());
            telemetry.addData("Direction", myBigServoArmMid.getDirection());
            telemetry.addData("Arm Claw", myBigServoClaw.getPower());
            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }
}
