/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 * *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 */
//@Disabled
@Autonomous(name="Red Left - Find SkyStones")
public class blueAutonomousVuforiaSkyStones extends LinearOpMode {

    /* Declare OpMode members. */
    greaseBBQLightning      robot   = new greaseBBQLightning();   // Use a Grease BBQ Lighting Hardware Setup
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = .5;
    static final double     TURN_SPEED              = 0.5;

    //Vuforia Stuff
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = FRONT;
    private static final boolean PHONE_IS_PORTRAIT = true;
    private static final String VUFORIA_KEY =
            "AQAe1Ff/////AAABmVOWpgy6pEJmogqArcRgMto5ygtSLgm+JYiAhW1QAESYbOvXP2dQAlnLyzMTrPf6+KvNMMLPS4Agbe/G2RcxOfnFU4UYl3EV013AIxWKOoHkZk7+d/AakkwUqcb4n2xovI311PUFiPTQRcNsCVgpP1tfBU5Vq5MA3nE31GeZYAKZluS7VTDjUqV8sLLSC3/1e9xrum64PdCoaJVCvIpqaN5CUw3ghVA34WmUFM7t6C15YC1JsUQXjqzL6AhPU2k/iWgwyKywfneb1qEP7rAACu4eg9H2WnQQr7uGDqpZAyHqliscmZgL8qIc0gKiPzb90PXhwIbAAowmTB4CAYBcKdMwIg9D70nvuNpaG5HrgOgm";

    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmPerInch        = 25.4f;
    private static final float mmTargetHeight   = (6) * mmPerInch;          // the height of the center of the target image above the floor

    // Constant for Stone Target
    private static final float stoneZ = 2.00f * mmPerInch;

    // Constants for the center support targets
    private static final float bridgeZ = 6.42f * mmPerInch;
    private static final float bridgeY = 23 * mmPerInch;
    private static final float bridgeX = 5.18f * mmPerInch;
    private static final float bridgeRotY = 59;                                 // Units are degrees
    private static final float bridgeRotZ = 180;

    // Constants for perimeter targets
    private static final float halfField = 72 * mmPerInch;
    private static final float quadField  = 36 * mmPerInch;

    // Class Members
    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia = null;
    private boolean targetVisible = false;
    private float phoneXRotate    = 0;
    private float phoneYRotate    = 0;
    private float phoneZRotate    = 0;

    private float x = 0;
    private float y = 0;
    private float z = 0;
    private double moveToX = 0;
    private double moveToY = 0;
    private boolean skyStoneFound = false;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * We can pass Vuforia the handle to a camera preview resource (on the RC phone);
         * If no camera monitor is desired, use the parameter-less constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection   = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        VuforiaTrackables targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");
        VuforiaTrackable blueRearBridge = targetsSkyStone.get(1);
        blueRearBridge.setName("Blue Rear Bridge");
        VuforiaTrackable redRearBridge = targetsSkyStone.get(2);
        redRearBridge.setName("Red Rear Bridge");
        VuforiaTrackable redFrontBridge = targetsSkyStone.get(3);
        redFrontBridge.setName("Red Front Bridge");
        VuforiaTrackable blueFrontBridge = targetsSkyStone.get(4);
        blueFrontBridge.setName("Blue Front Bridge");
        VuforiaTrackable red1 = targetsSkyStone.get(5);
        red1.setName("Red Perimeter 1");
        VuforiaTrackable red2 = targetsSkyStone.get(6);
        red2.setName("Red Perimeter 2");
        VuforiaTrackable front1 = targetsSkyStone.get(7);
        front1.setName("Front Perimeter 1");
        VuforiaTrackable front2 = targetsSkyStone.get(8);
        front2.setName("Front Perimeter 2");
        VuforiaTrackable blue1 = targetsSkyStone.get(9);
        blue1.setName("Blue Perimeter 1");
        VuforiaTrackable blue2 = targetsSkyStone.get(10);
        blue2.setName("Blue Perimeter 2");
        VuforiaTrackable rear1 = targetsSkyStone.get(11);
        rear1.setName("Rear Perimeter 1");
        VuforiaTrackable rear2 = targetsSkyStone.get(12);
        rear2.setName("Rear Perimeter 2");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsSkyStone);

        /**
         * In order for localization to work, we need to tell the system where each target is on the field, and
         * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
         * Transformation matrices are a central, important concept in the math here involved in localization.
         * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
         * for detailed information. Commonly, you'll encounter transformation matrices as instances
         * of the {@link OpenGLMatrix} class.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         *     - The X axis runs from your left to the right. (positive from the center to the right)
         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         *
         * Before being transformed, each target image is conceptually located at the origin of the field's
         *  coordinate system (the center of the field), facing up.
         */

        // Set the position of the Stone Target.  Since it's not fixed in position, assume it's at the field origin.
        // Rotated it to to face forward, and raised it to sit on the ground correctly.
        // This can be used for generic target-centric approach algorithms
        stoneTarget.setLocation(OpenGLMatrix
                .translation(0, 0, stoneZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        //Set the position of the bridge support targets with relation to origin (center of field)
        blueFrontBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, bridgeRotZ)));

        blueRearBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, bridgeRotZ)));

        redFrontBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, 0)));

        redRearBridge.setLocation(OpenGLMatrix
                .translation(bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, 0)));

        //Set the position of the perimeter targets with relation to origin (center of field)
        red1.setLocation(OpenGLMatrix
                .translation(quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        red2.setLocation(OpenGLMatrix
                .translation(-quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        front1.setLocation(OpenGLMatrix
                .translation(-halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90)));

        front2.setLocation(OpenGLMatrix
                .translation(-halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

        blue1.setLocation(OpenGLMatrix
                .translation(-quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        blue2.setLocation(OpenGLMatrix
                .translation(quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        rear1.setLocation(OpenGLMatrix
                .translation(halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , -90)));

        rear2.setLocation(OpenGLMatrix
                .translation(halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        //
        // Create a transformation matrix describing where the phone is on the robot.
        //
        // NOTE !!!!  It's very important that you turn OFF your phone's Auto-Screen-Rotation option.
        // Lock it into Portrait for these numbers to work.
        //
        // Info:  The coordinate frame for the robot looks the same as the field.
        // The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
        // Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
        //
        // The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
        // pointing to the LEFT side of the Robot.
        // The two examples below assume that the camera is facing forward out the front of the robot.

        // We need to rotate the camera around it's long axis to bring the correct camera forward.
        if (CAMERA_CHOICE == BACK) {
            phoneYRotate = -90;
        } else {
            phoneYRotate = 90;
        }

        // Rotate the phone vertical about the X axis if it's in portrait mode
        if (PHONE_IS_PORTRAIT) {
            phoneXRotate = 90 ;
        }

        // Next, translate the camera lens to where it is on the robot.
        // In this example, it is centered (left to right), but forward of the middle of the robot, and above ground level.
        final float CAMERA_FORWARD_DISPLACEMENT  = 3.0f * mmPerInch;   // eg: Camera is 3 Inches in front of robot center
        final float CAMERA_VERTICAL_DISPLACEMENT = 8.5f * mmPerInch;   // eg: Camera is 8.45 Inches above ground
        final float CAMERA_LEFT_DISPLACEMENT     = 0;     // eg: Didn't seem to affect output

        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

        /**  Let all the trackable listeners know where the phone is.  */
        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }

        robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.myBigMotorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.myBigMotorRandP.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.myBigMotorRandP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        //robot.myBigServoFoundation.setPower(.3);

        //driveToFoundation(1);

        //telemetry.addData("Path", "Complete");
        //telemetry.update();

            targetsSkyStone.activate();
            while (runtime.seconds()<30) {
                robot.myBigMotorBackLeft.setPower(.1);
                robot.myBigMotorBackRight.setPower(-.1);
                robot.myBigMotorFrontLeft.setPower(-.1);
                robot.myBigMotorFrontRight.setPower(.1);
                // check all the trackable targets to see which one (if any) is visible.
                targetVisible = false;
                for (VuforiaTrackable trackable : allTrackables) {
                    if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                        telemetry.addData("Visible Target", trackable.getName());
                        targetVisible = true;

                        // getUpdatedRobotLocation() will return null if no new information is available since
                        // the last time that call was made, or if the trackable is not currently visible.
                        OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                        if (robotLocationTransform != null) {
                            lastLocation = robotLocationTransform;
                        }
                        break;
                    }
                }

                // Provide feedback as to where the robot is located (if we know).
                if (targetVisible) {
                    robot.myBigMotorBackLeft.setPower(0);
                    robot.myBigMotorBackRight.setPower(0);
                    robot.myBigMotorFrontLeft.setPower(0);
                    robot.myBigMotorFrontRight.setPower(0);

                    // express position (translation) of robot in inches.
                    VectorF translation = lastLocation.getTranslation();
                    x = (translation.get(0) / mmPerInch) + 2.5f;
                    y = (translation.get(1) / mmPerInch) - 2.5f;
                    z = (translation.get(2) / mmPerInch) - 2.5f;
                    moveToX = x + 5.5;
                    moveToY = y + 3.5;
                    skyStoneFound = true;

                    telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                            x, y, z);
                    // express the rotation of the robot in degrees.
                    Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
                    telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
                }
                else {
                    telemetry.addData("Visible Target", "none");
                }
                telemetry.update();

                if(skyStoneFound){
                    targetsSkyStone.deactivate();
                    //Reset Encoders (dummy)
                    robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    robot.myBigMotorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                    encoderDrive(.1,moveToY,-moveToY,-moveToY,moveToY);
                    encoderDrive(.1,-moveToX,-moveToX,-moveToX,-moveToX);

                    //encoderDrive(.3,20,20,20,20);
                    robot.myBigServoClaw.setPower(1);

                    //encoderDrive(.5,-20,-20,-20,-20);
                    skyStoneFound = false;
                    targetsSkyStone.activate();
                }
            }



        targetsSkyStone.deactivate();
            // Disable Tracking when we are done;

        }


    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double fleftInches, double frightInches,
                             double bleftInches, double brightInches) {
        int newfLeftTarget;
        int newfRightTarget;
        int newbLeftTarget;
        int newbRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {


            // Determine new target position, and pass to motor controller
            newfLeftTarget = robot.myBigMotorFrontLeft.getCurrentPosition() + (int)(-fleftInches * COUNTS_PER_INCH);
            newfRightTarget = robot.myBigMotorFrontRight.getCurrentPosition() + (int)(-frightInches * COUNTS_PER_INCH);
            newbLeftTarget = robot.myBigMotorFrontLeft.getCurrentPosition() + (int)(-bleftInches * COUNTS_PER_INCH);
            newbRightTarget = robot.myBigMotorFrontRight.getCurrentPosition() + (int)(-brightInches * COUNTS_PER_INCH);
            robot.myBigMotorFrontLeft.setTargetPosition(newfLeftTarget);
            robot.myBigMotorFrontRight.setTargetPosition(newfRightTarget);
            robot.myBigMotorBackLeft.setTargetPosition(newbLeftTarget);
            robot.myBigMotorBackRight.setTargetPosition(newbRightTarget);

            // Turn On RUN_TO_POSITION
            robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //start motion.
            robot.myBigMotorFrontLeft.setPower(abs(speed));
            robot.myBigMotorFrontRight.setPower(abs(speed));
            robot.myBigMotorBackLeft.setPower(abs(speed));
            robot.myBigMotorBackRight.setPower(abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.

            while (opModeIsActive()
                    //&&
                   //(runtime.seconds() < timeoutS)
                    &&
                    (robot.myBigMotorFrontLeft.isBusy() && robot.myBigMotorFrontRight.isBusy()
                            && robot.myBigMotorBackLeft.isBusy() && robot.myBigMotorBackRight.isBusy())
            )

            {

                // Display it for the driver.
                //telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d :%7d :%7d",
                                            robot.myBigMotorFrontLeft.getCurrentPosition(),
                                            robot.myBigMotorFrontRight.getCurrentPosition(),
                                            robot.myBigMotorBackLeft.getCurrentPosition(),
                                            robot.myBigMotorBackRight.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.myBigMotorFrontLeft.setPower(0);
            robot.myBigMotorFrontRight.setPower(0);
            robot.myBigMotorBackLeft.setPower(0);
            robot.myBigMotorBackRight.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    public void driveToFoundation(double speed){
        //Raise RandP
        robot.setRackAndPinionHeight(1440,1);
        //Move off of wall
        encoderDrive(.5,4,4,4,4);
        //Strafing left to center to foundation
        encoderDrive(.5,-6,6,6,-6);
        //Move to foundation
        encoderDrive(.5,25,25,25,25);
        //Drop RandP onto Foundation
        robot.setRackAndPinionHeight(0,1);
        //Wait for RandP to land on foundation
        sleep(1000);
        //Pull foundation backwards to wall
        encoderDrive(.5,-28,-28,-28,-28);
        //Lift RandP off of foundation before strafing to park
        robot.setRackAndPinionHeight(1440,1);
        //Wait for RandP to clear foundation before strafing
        sleep(1000);
        //Start strafing toward parking zone
        encoderDrive(1,20,-20,-20,20);
        //Set RandP back down to start position
        robot.setRackAndPinionHeight(0,1);
        sleep(1000);
        //Finish strafing to parking zone
        encoderDrive(1,23,-23,-23,23);
    }
}
