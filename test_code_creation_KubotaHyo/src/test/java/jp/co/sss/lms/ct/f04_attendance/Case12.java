package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// TODO ここに追加
		goTo("http://localhost:8080/lms");
		getEvidence(new Object() {}, "test01");
		String pageTitle = WebDriverUtils.webDriver.getTitle();
		assertEquals("ログイン | LMS", pageTitle);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// TODO ここに追加
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA01");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA02");
		webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		WebElement courseDetail = wait.until(
				ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector("ol.breadcrumb li.active")
				)
		);

		getEvidence(new Object() {}, "test02");

		assertEquals("コース詳細", courseDetail.getText());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {

		WebElement attendance = webDriver.findElement(By.linkText("勤怠"));
		attendance.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		WebElement attendanceManagement = webDriver.findElement(By.tagName("h2"));
		assertEquals("勤怠管理", attendanceManagement.getText());

		getEvidence(new Object() {}, "test03");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		// TODO ここに追加
		WebElement editInfo = webDriver.findElement(By.linkText("勤怠情報を直接編集する"));
		editInfo.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector(".update-button")
		));

		getEvidence(new Object() {}, "test04");

		assertTrue(webDriver.getTitle().contains("勤怠"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() throws InterruptedException {
		// TODO ここに追加
		new Select(webDriver.findElement(By.id("startHour0"))).selectByIndex(0);
		new Select(webDriver.findElement(By.id("endHour0"))).selectByIndex(0);

		WebElement updateBtn = webDriver.findElement(By.cssSelector(".btn-info.update-button"));

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", updateBtn);
		Thread.sleep(300);
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", updateBtn);

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		getEvidence(new Object() {}, "test05");

		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector(".help-inline.error")
		));

		List<WebElement> errors = webDriver.findElements(
				By.cssSelector(".help-inline.error")
		);

		List<String> texts = errors.stream().map(WebElement::getText).toList();

		assertTrue(texts.contains("出勤時間が正しく入力されていません。"));
		assertTrue(texts.contains("退勤時間が正しく入力されていません。"));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() throws InterruptedException {
		// TODO ここに追加
		new Select(webDriver.findElement(By.id("endHour0"))).selectByIndex(19);

		WebElement updateBtn = webDriver.findElement(By.cssSelector(".btn-info.update-button"));

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", updateBtn);
		Thread.sleep(300);
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", updateBtn);

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		getEvidence(new Object() {}, "test06");

		WebElement errorMsg = wait.until(
				ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector(".help-inline.error")
				)
		);

		assertEquals("出勤時間が正しく入力されていません。", errorMsg.getText());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() throws InterruptedException {
		// TODO ここに追加
		new Select(webDriver.findElement(By.id("startHour0"))).selectByIndex(20);

		WebElement updateBtn = webDriver.findElement(By.cssSelector(".btn-info.update-button"));

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", updateBtn);
		Thread.sleep(300);
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", updateBtn);

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		getEvidence(new Object() {}, "test07");

		WebElement errorMsg = wait.until(
				ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector(".help-inline.error")
				)
		);

		assertTrue(errorMsg.getText().contains("退勤時刻[0]は出勤時刻[0]より後でなければいけません。"));
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() throws InterruptedException {
		// TODO ここに追加
		new Select(webDriver.findElement(By.id("startHour0"))).selectByIndex(18);

		Select dropdownStepOut = new Select(
				webDriver.findElement(By.cssSelector("select[name='attendanceList[0].blankTime']"))
		);
		dropdownStepOut.selectByIndex(20);

		WebElement updateBtn = webDriver.findElement(By.cssSelector(".btn-info.update-button"));

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", updateBtn);
		Thread.sleep(300);
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", updateBtn);

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		getEvidence(new Object() {}, "test08");

		WebElement errorMsg = wait.until(
				ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector(".help-inline.error")
				)
		);

		assertTrue(errorMsg.getText().contains("中抜け時間が勤務時間を超えています"));
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() throws InterruptedException {
		// TODO ここに追加
		new Select(webDriver.findElement(
				By.cssSelector("select[name='attendanceList[0].blankTime']")
		)).selectByIndex(0);

		WebElement note = webDriver.findElement(
				By.cssSelector("input[name='attendanceList[0].note']")
		);

		String aRpeat = "a".repeat(101);
		note.sendKeys(aRpeat);

		WebElement updateBtn = webDriver.findElement(By.cssSelector(".btn-info.update-button"));

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", updateBtn);
		Thread.sleep(300);
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", updateBtn);

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();

		getEvidence(new Object() {}, "test09");

		WebElement errorMsg = wait.until(
				ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector(".help-inline.error")
				)
		);

		assertTrue(errorMsg.getText().contains("備考の長さが最大値(100)を超えています。"));
	}
}
