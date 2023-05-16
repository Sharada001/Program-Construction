import java.util.ArrayList;
import java.util.Random;

class StudyRoom {
    // Private attributes of Study Room objects
    private int roomNumber, capacity, reservedNumber=0;
    private boolean availabilityStatus=true;

    // Initialize Study Room with roomNumber and Capacity
    public StudyRoom(int roomNumber, int capacity) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    // getter for roomNumber
    public int getRoomNumber() { return roomNumber; }

    // setter for roomNumber
    public void setRoomNumber(int value) { roomNumber = value; }

    // getter for capacity
    public int getCapacity() { return capacity; }

    // setter for capacity
    public void setCapacity(int value) { capacity = value; }

    // getter for availabilityStatus
    public boolean getAvailabilityStatus() { return availabilityStatus; }

    // setter for roomNumber
    public void setAvailabilityStatus(boolean value) { availabilityStatus = value; }

    // getter for reservedNumber
    public int getReservedNumber() { return reservedNumber; }

    // setter for roomNumber
    public void setReservedNumber(int value) { reservedNumber = value; }
}

class StudyRoomUnavailableException extends InterruptedException {
    // Interrupted Exception to generate in case of trying to reserve
    // an unavailable study room
    public StudyRoomUnavailableException(int number){
        super("Study Room "+number+" is unavailable");
    }
}

class ReserveStudyRoom extends Thread {
    // Thread to Reserve a StudyRoom based on its Availability

    // Initialize Thread with given StudyRoom object
    private final StudyRoom studyRoom;
    public ReserveStudyRoom(StudyRoom studyRoom) {
        this.studyRoom = studyRoom;
    }

    @Override
    public void run(){
        // Disable other threads accessing current Thread's StudyRoom object
        // until end of reserving operation
        synchronized (studyRoom) {
            try {
                // check for availability of StudyRoom
                if (studyRoom.getAvailabilityStatus()) {
                    studyRoom.setReservedNumber(studyRoom.getReservedNumber() + 1);
                    studyRoom.setAvailabilityStatus(studyRoom.getReservedNumber() != studyRoom.getCapacity());
                }
                // if StudyRoom is unavailable, throw StudyRoomUnavailableException
                else {
                    throw new StudyRoomUnavailableException(studyRoom.getRoomNumber());
                }
            }
            // handle thrown StudyRoomUnavailableException
            catch (StudyRoomUnavailableException ex) {
                // System.out.println(ex);
            }
        }
    }
}

class ReleaseStudyRoom extends Thread {
    // Thread to Release space of a StudyRoom

    // Initialize Thread with given StudyRoom object
    private final StudyRoom studyRoom;
    public ReleaseStudyRoom(StudyRoom studyRoom) {
        this.studyRoom = studyRoom;
    }
    @Override
    public void run() {
        // Disable other threads accessing current Thread's StudyRoom object
        // until end of releasing operation
        synchronized (studyRoom) {
            try {
                // If StudyRoom is currently unavailable, make it available
                if (!studyRoom.getAvailabilityStatus()) {
                    studyRoom.setAvailabilityStatus(!studyRoom.getAvailabilityStatus());
                }
                // If StudyRoom is empty, throw an exception
                if (studyRoom.getReservedNumber() == 0) {
                    throw new InterruptedException("Study Room is empty");
                }
                studyRoom.setReservedNumber(studyRoom.getReservedNumber() - 1);
            }
            // handle the exception
            catch (InterruptedException ex) {
                // System.out.println(ex);
            }
        }
    }
}

public class StudyRoomReservationSystem {

    // ArrayList to store created StudyRoom objects
    private final ArrayList<StudyRoom> availableStudyRooms = new ArrayList<StudyRoom>();

    // ArrayList to store created Threads
    private static ArrayList<Thread> threads = new ArrayList<Thread>();

    // Method to get number of study rooms in reservation System
    private int getNumberOfStudyRooms() {
        return availableStudyRooms.size();
    }

    // Reserve a slot of given StudyRoom
    public boolean reserveStudyRoom(int roomNumber) {
        for(StudyRoom sr: availableStudyRooms) {
            // loop through ArrayList to find out StudyRoom with given roomNumber
            if (sr.getRoomNumber()==roomNumber) {
                ReserveStudyRoom rsr = new ReserveStudyRoom(sr);
                threads.add(rsr);
                // start execution of reserving a slot of StudyRoom
                rsr.start();
                return true;
            }
        }
        return false;
    }

    // Release a slot of given StudyRoom
    public boolean releaseStudyRoom(int roomNumber) {
        for(StudyRoom sr: availableStudyRooms) {
            // loop through ArrayList to find out StudyRoom with given roomNumber
            if (sr.getRoomNumber()==roomNumber) {
                ReleaseStudyRoom rsr = new ReleaseStudyRoom(sr);
                threads.add(rsr);
                // start execution of releasing a slot of StudyRoom
                rsr.start();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    public void displayStudyRoomStatus() {
        // method to display roomNumber, Capacity and current Availability of all StudyRooms
        System.out.println("Study Room Status:");
        for(StudyRoom sr: availableStudyRooms) {
            System.out.println("Room Number: "+sr.getRoomNumber()+", Capacity: "+sr.getCapacity()+", Availability: "+((sr.getAvailabilityStatus()) ? "Available" : "Unavailable"));
        }
    }
    public void createStudyRoom(int roomNumber, int capacity) {
        // Initialize a StudyRoom object and add it to ArrayList of StudyRoom objects
        StudyRoom studyRoom = new StudyRoom(roomNumber, capacity);
        availableStudyRooms.add(studyRoom);
    }

    public boolean isAnyActiveThread() {
        boolean state = false;
        // loop through threads to figure out whether, there is any active thread
        for (Thread t: threads) {
            if (t.isAlive()) {
                state = true;
                 break;
            }
        }
        return state;
    }
    public void test(int numOfRooms) {
        // randomly reserve and release slots of study rooms
        Random random = new Random();
        if (random.nextInt(2)==1) {
            reserveStudyRoom(random.nextInt(numOfRooms)+1);
        } else {
            releaseStudyRoom(random.nextInt(numOfRooms)+1);
        }
    }
    public static void main(String[] args) {

        // create a StudyRoomReservationSystem object
        StudyRoomReservationSystem srrs = new StudyRoomReservationSystem();

        // create multiple StudyRoom objects
        srrs.createStudyRoom(1,4);
        srrs.createStudyRoom(2,6);
        srrs.createStudyRoom(3,8);
        srrs.createStudyRoom(4,5);
        srrs.createStudyRoom(5,7);
        srrs.displayStudyRoomStatus();

        // Randomly try to reserve and release from StudyRoom objects multiple times
        for (int i=0; i<60; i++) {
            srrs.test(srrs.getNumberOfStudyRooms());
        }


        while (true) {
            try {
                // check whether all threads have finished their works
                if(!srrs.isAnyActiveThread()) {
                    // if all threads have finished their works print states of StudyRooms
                    srrs.displayStudyRoomStatus();
                    break;
                }
                // if threads have not finished work, wait for 1 millisecond
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }
}
