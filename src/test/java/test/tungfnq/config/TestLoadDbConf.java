/**
 * 
 */
package test.tungfnq.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tungfnq.bean.SysParam;

/**
 * @author tungnq
 *
 */
public class TestLoadDbConf {
	 public static void main(String []args) {
		 try {
	        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	        
	        SysParam sysParam = (SysParam)applicationContext.getBean("sysParam");
	        System.out.println(sysParam);
	        
	        sysParam = (SysParam)applicationContext.getBean("sysParam1");
	        System.out.println(sysParam);
	        
	        sysParam = (SysParam)applicationContext.getBean("sysParam2");
	        System.out.println(sysParam);
	        
	    }catch( Exception ex ) {
	            ex.printStackTrace();
	    }
	}
}
