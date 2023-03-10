package in.co.rays.model.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.RoleModel;

/**
 * Role Model Test classes
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 *
 */
public class RoleModelTest {
	/**
     * Model object to test
     */

    public static RoleModel model = new RoleModel();

    /**
     * Main method to call test methods.
     *
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
         //testAdd();
        // testDelete();
         //testUpdate();
         //testFindByPK();
        //testFindByName();
         testSearch();
        //testList();

    }

    /**
     * Tests add a Role
     *
     * @throws ParseException
     */
    public static void testAdd() throws ParseException {

        try {
            RoleBean bean = new RoleBean();
            // SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            // bean.setId(1L);
            bean.setName("Monty");
            bean.setDescription("yadav");
            bean.setCreatedBy("Monty");
            bean.setModifiedBy("Monty");
            long pk = model.add(bean);
            RoleBean addedbean = model.findByPK(pk);
            if (addedbean == null) {
                System.out.println("Test add fail");
            }
        } catch (ApplicationException e) {
            e.printStackTrace();
        } catch (DuplicateRecordException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tests delete a Role
     */
    public static void testDelete() {

        try {
            RoleBean bean = new RoleBean();
            long pk = 1L;
            bean.setId(pk);
            model.delete(bean);
            RoleBean deletedbean = model.findByPK(pk);
            if (deletedbean != null) {
                System.out.println("Test Delete fail");
            }
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        System.out.println("Record Deleted");
    }

    /**
     * Tests update a Role
     */
    public static void testUpdate() {

        try {
            RoleBean bean = model.findByPK(5L);
           // bean.setName("");
            bean.setDescription("Sister");
            
            model.update(bean);

            RoleBean updatedbean = model.findByPK(1L);
//            if (!"12".equals(updatedbean.getName())) {
//                System.out.println("Test Update fail");
//            }
        } catch (ApplicationException e) {
            e.printStackTrace();
        } catch (DuplicateRecordException e) {
            e.printStackTrace();
        }
        System.out.println("Record Updated");
    }

    /**
     * Tests find a User by PK.
     */
    public static void testFindByPK() {
        try {
            RoleBean bean = new RoleBean();
            long pk = 5L;
            bean = model.findByPK(pk);
            if (bean == null) {
                System.out.println("Test Find By PK fail");
            }
            System.out.println(bean.getId());
            System.out.println(bean.getName());
            System.out.println(bean.getDescription());
        } catch (ApplicationException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tests find a User by Name.
     */
    public static void testFindByName() {
        try {
            RoleBean bean = new RoleBean();
            bean = model.findByName("sonali");
            if (bean == null) {
                System.out.println("Test Find By PK fail");
            }
            System.out.println(bean.getId());
            System.out.println(bean.getName());
            System.out.println(bean.getDescription());
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests get Search
     */
    public static void testSearch() {

        try {
            RoleBean bean = new RoleBean();
            List list = new ArrayList();
            bean.setName("sonali");
            list = model.search(bean, 0, 0);
            if (list.size() < 0) {
                System.out.println("Test Serach fail");
            }
            Iterator it = list.iterator();
            while (it.hasNext()) {
                bean = (RoleBean) it.next();
                System.out.println(bean.getId());
                System.out.println(bean.getName());
                System.out.println(bean.getDescription());
            }

        } catch (ApplicationException e) {
            e.printStackTrace();
        }

    }

    //Tests get List.
 
    public static void testList() {

        try {
            RoleBean bean = new RoleBean();
            List list = new ArrayList();
            list = model.list(1, 10);
            if (list.size() < 0) {
                System.out.println("Test list fail");
            }
            Iterator it = list.iterator();
            while (it.hasNext()) {
                bean = (RoleBean) it.next();
                System.out.println(bean.getId());
                System.out.println(bean.getName());
                System.out.println(bean.getDescription());
            }

        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

}
