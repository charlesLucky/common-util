import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlUtil {
	public static Object xml2Bean(String result, String className) {
		try {
			Document document = DocumentHelper.parseText(result);
			Element root = document.getRootElement();
			Class c = Class.forName(className);
			Field[] properties = c.getDeclaredFields();// 获得实例的属性
			Object obj = c.newInstance();// 获得对象的新的实例
			for (Field field : properties) {
				String methName = field.getName();
				Method setmeth = obj.getClass().getMethod(method_set(methName),
						field.getType());
				String name = field.getType().getName();
				String value = ((Element) root).elementText(methName);
				if (!Common.isEmpty(value)) {
					value = value.trim();
					if (name.contains("int") || name.contains("Integer")) {
						setmeth.invoke(obj,
								new Object[] { Integer.parseInt(value) });
					} else if (name.contains("double")
							|| name.contains("Double")) {
						setmeth.invoke(obj,
								new Object[] { Double.parseDouble(value) });
					} else if (name.contains("String")) {
						setmeth.invoke(obj, new Object[] { value });
					} else if (name.contains("Long") || name.contains("long")) {
						setmeth.invoke(obj,
								new Object[] { Long.parseLong(value) });
					} else if (name.contains("Date")) {
						setmeth.invoke(obj, new Object[] { Date.parse(value) });
					}
				}
			}
			return obj;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String method_get(String methodName) {
		return "get" + methodName.substring(0, 1).toUpperCase()
				+ methodName.substring(1);
	}

	public static String method_set(String methodName) {
		return "set" + methodName.substring(0, 1).toUpperCase()
				+ methodName.substring(1);
	}

	public static <T> void beans2XmlFile(Class cls, List<T> entityPropertys,
			String Encode, String XMLPathAndName) {
		long lasting = System.currentTimeMillis();// 效率检测
		try {
			XMLWriter writer = null;// 声明写XML的对象
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(Encode);// 设置XML文件的编码格式

			String filePath = XMLPathAndName;// 获得文件地址
			File file = new File(filePath);// 获得文件
			if (file.exists()) {
				file.delete();
			}
			// 新建student.xml文件并新增内容
			Document document = DocumentHelper.createDocument();
			String rootname = cls.getSimpleName();// 获得类名
			Element root = document.addElement(rootname + "s");// 添加根节点
			Field[] properties = cls.getDeclaredFields();// 获得实体类的所有属性

			for (T t : entityPropertys) { // 递归实体
				Element secondRoot = root.addElement(rootname); // 二级节点

				for (int i = 0; i < properties.length; i++) {
					// 反射get方法
					Method meth = t.getClass().getMethod(
							"get"
									+ properties[i].getName().substring(0, 1)
											.toUpperCase()
									+ properties[i].getName().substring(1));
					// 为二级节点添加属性，属性值为对应属性的值
					secondRoot.addElement(properties[i].getName()).setText(
							meth.invoke(t).toString());

				}
			}
			// 生成XML文件
			writer = new XMLWriter(new FileWriter(file), format);
			writer.write(document);
			writer.close();
			long lasting2 = System.currentTimeMillis();
			System.out.println("写入XML文件结束,用时" + (lasting2 - lasting) + "ms");
		} catch (Exception e) {
			System.out.println("XML文件写入失败");
		}

	}

	public static <T> String beans2XmlStr(Class cls, List<T> entityPropertys) {
		long lasting = System.currentTimeMillis();// 效率检测
		Document document = null;
		try {

			// 新建student.xml文件并新增内容
			document = DocumentHelper.createDocument();
			String rootname = cls.getSimpleName();// 获得类名
			Element root = document.addElement(rootname + "s");// 添加根节点
			Field[] properties = cls.getDeclaredFields();// 获得实体类的所有属性

			for (T t : entityPropertys) { // 递归实体
				Element secondRoot = root.addElement(rootname); // 二级节点

				for (int i = 0; i < properties.length; i++) {
					// 反射get方法
					Method meth = t.getClass().getMethod(
							"get"
									+ properties[i].getName().substring(0, 1)
											.toUpperCase()
									+ properties[i].getName().substring(1));
					// 为二级节点添加属性，属性值为对应属性的值
					secondRoot.addElement(properties[i].getName()).setText(
							meth.invoke(t).toString());

				}
			}

			long lasting2 = System.currentTimeMillis();
			System.out.println("写入XML文件结束,用时" + (lasting2 - lasting) + "ms");
		} catch (Exception e) {
			System.out.println("XML文件写入失败");
		}
		return document.asXML();
	}

	// 获取一个xml里面多个bean
	@SuppressWarnings("unchecked")
	public static <T> List<T> xml2Beans(String XMLStr, Class cls) {
		long lasting = System.currentTimeMillis();// 效率检测
		List<T> list = new ArrayList<T>();// 创建list集合
		try {
			Document doc = DocumentHelper.parseText(XMLStr);// dom4j读取
			Element root = doc.getRootElement();// 获得根节点
			Element foo;// 二级节点
			Field[] properties = cls.getDeclaredFields();// 获得实例的属性
			// 实例的get方法
			Method getmeth;
			// 实例的set方法
			Method setmeth;

			for (Iterator i = root.elementIterator(cls.getSimpleName()); i
					.hasNext();) {// 遍历t.getClass().getSimpleName()节点
				foo = (Element) i.next();// 下一个二级节点

				T t = (T) cls.newInstance();// 获得对象的新的实例

				for (int j = 0; j < properties.length; j++) {// 遍历所有孙子节点

					// 实例的set方法
					setmeth = cls.getMethod("set"
							+ properties[j].getName().substring(0, 1)
									.toUpperCase()
							+ properties[j].getName().substring(1),
							properties[j].getType());
					// properties[j].getType()为set方法入口参数的参数类型(Class类型)
					setmeth.invoke(t, foo.elementText(properties[j].getName()));// 将对应节点的值存入

				}
				list.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long lasting2 = System.currentTimeMillis();
		System.out.println("解析XML文件结束,用时" + (lasting2 - lasting) + "ms");
		return list;
	}

	public static void main(String[] args) {
		User user = new User();
		User user1 = new User("姓名1", "18", "男");
		User user2 = new User("姓名2", "19", "女");
		User user3 = new User("石头", "20", "女");
		List<User> users = new ArrayList<User>();
		users.add(user1);
		users.add(user2);
		users.add(user3);
		System.out.println(XmlUtil.beans2XmlStr(User.class, users));
	}

}
