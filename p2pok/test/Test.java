import business.User;

public class Test {

	public static void main(String[] args) {

		int id = 5;
		for (int i = 0; i < 100; i++) {
			final int ii = i;
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					new Test().testlock(5);
				}
			}).start();;
		}
	}

	public void testlock(int id) {

//		ReentrantLockTest.getInstance().addLock(id);
////		System.out.println("im working id is:" + id);
//		ReentrantLockTest.getInstance().unLock(id);
		
		
		User.addLock(id);
		
		User.deleteLock(id);
		
	}

	
	
	
}