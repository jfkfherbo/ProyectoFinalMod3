package employee;

import conf.BaseTest;
import org.orangehrm.helpers.JsonTestDataHelper;
import org.orangehrm.models.EmployeeData;
import org.orangehrm.pages.AttachmentsPage;
import org.orangehrm.pages.EmployeeListPage;
import org.orangehrm.pages.LoginPage;
import org.orangehrm.pages.PersonalDetailsPage;
import org.orangehrm.pages.PimPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public class EmployeeWorkflowTest extends BaseTest {
    private static final String DATA_PATH = "resources/testdata/employees.json";

    @Test(description = "Crear empleado, completar sus datos y verificar el adjunto",
            dataProvider = "employees")
    public void createAndCompleteEmployee(EmployeeData employee) {
        String firstName = employee.getFirstName();
        String loginUser = employee.getLoginUser();

        new LoginPage(driver).login(employee.getAdminUser(), employee.getAdminPass());

        PimPage pimPage = new PimPage(driver);
        pimPage.navigateToPim();
        String employeeId = pimPage.createEmployee(firstName, employee.getMiddleName(),
                employee.getLastName(), employee.isCreateLoginDetails(), loginUser, employee.getLoginPass());
        Assert.assertTrue(employeeId != null && !employeeId.isBlank(), "El sistema debe asignar un Employee Id.");

        EmployeeListPage employeeList = new EmployeeListPage(driver);
        employeeList.searchEmployeeById(employeeId);
        Assert.assertTrue(employeeList.hasResults(), "El empleado debe aparecer en la grilla.");
        Assert.assertEquals(employeeList.getFirstResultId(), employeeId,
                "El Employee Id de la grilla debe coincidir con el creado.");

        employeeList.editFirstEmployee();
        PersonalDetailsPage details = new PersonalDetailsPage(driver);
        details.waitForEmployeeId(employeeId);
        Assert.assertEquals(details.getEmployeeId(), employeeId,
                "El Employee Id de Personal Details debe coincidir con el creado.");

        details.completePersonalDetails(employee);
        Assert.assertEquals(details.getOtherId(), employee.getOtherId(), "Other Id debe guardarse.");
        Assert.assertEquals(details.getNationality(), employee.getNationality(), "Nationality debe guardarse.");
        Assert.assertEquals(details.getMaritalStatus(), employee.getMaritalStatus(), "Marital Status debe guardarse.");
        details.completeCustomFields(employee);
        Assert.assertEquals(details.getBloodType(), employee.getBloodType(), "Blood Type debe guardarse.");
        Assert.assertEquals(details.getCustomField(), employee.getCustomField(), "El campo personalizado debe guardarse.");

        AttachmentsPage attachments = new AttachmentsPage(driver);
        attachments.addAttachment(employee.getAttachmentPath(), employee.getAttachmentComment());
        String fileName = Path.of(employee.getAttachmentPath()).getFileName().toString();
        Assert.assertTrue(attachments.attachmentIsDisplayed(fileName),
                "El archivo adjunto debe aparecer en la lista del empleado.");
    }

    @DataProvider(name = "employees")
    public Object[] employees() throws FileNotFoundException {
        return JsonTestDataHelper.getInstance().getTestData(DATA_PATH, EmployeeData.class);
    }
}
