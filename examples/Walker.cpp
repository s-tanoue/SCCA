#include "Walker.h"




Walker::Walker(){
}


/*aa*/
typedef /*aa*/  struct employee {
    char name[12];
    double hours;
    int wage;
} EMPLOYEE;

void Walker::angleChange(int angle, int rotation) {

    StretchVector *vector;
    int *ta = NULL;

    int32_t defaultAngleL; /*int b*/

    int8_t dAngle = 75;   //int 8
    int32_t dAngle =21;

    if(angle % 5 == 0 && angle % 45 != 0) {
        dAngle = 8;
        angle /= 5;
    } else {
        angle -= angle % 45;
        angle /= 45;
    }
    for(int i=0; i<n; i++){
    }

    defaultAngleL = leftWheel.getCount();
    //test
    while(1) {
        run(0, 10 * rotation);
        if(rotation >= 0) {
        //ハロー
            if(leftWheel.getCount() - defaultAngleL < -dAngle * angle * rotation ||
                leftWheel.getCount() - defaultAngleL > dAngle * angle * rotation) {
                break;
            }
        }//ハロー
         else {
            if(leftWheel.getCount() - defaultAngleL > -dAngle * angle * rotation ||
                leftWheel.getCount() - defaultAngleL < dAngle * angle * rotation) {
                break;
            }
        }
        clock.sleep(4);
    }
    stop();
}



int Walker::edgeChange() {
    if(leftRight == 1) {
        run(10, 5);
        clock.sleep(10);
        leftRight = -1;
    } else {
        run(10, 5);
        clock.sleep(10);
        leftRight = 1;
    }

    return leftRight;
}

Walker::~Walker(){
}
