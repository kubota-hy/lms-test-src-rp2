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
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト 勤怠管理機能
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {

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
		getEvidence(new Object() {},"test01");
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

		//DOM構築前に検索するとエラーになるため、表示されるまで待機する
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		WebElement courseDetail = wait.until(
				ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector("ol.breadcrumb li.active")
						)
				);
		getEvidence(new Object() {},"test02");

		assertEquals("コース詳細", courseDetail.getText());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {

		WebElement attendance = webDriver.findElement(By.linkText("勤怠"));
		attendance.click();
		
		Alert alert = webDriver.switchTo().alert();
		alert.accept();


		WebElement attendanceManagement = webDriver.findElement(By.tagName("h2"));
		assertEquals("勤怠管理", attendanceManagement.getText());

		//画面をキャプチャして保存する
		getEvidence(new Object() {},"test03");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		// TODO ここに追加
		WebElement editInfo = webDriver.findElement(By.linkText("勤怠情報を直接編集する"));
		
		editInfo.click();
		
		 // 遷移先特有の要素が出るまで待つ
	    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
	    wait.until(ExpectedConditions.visibilityOfElementLocated(
	        By.cssSelector(".update-button")
	    ));

		getEvidence(new Object() {},"test04");

		assertTrue(webDriver.getTitle().contains("勤怠"));

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() throws InterruptedException {
		// TODO ここに追加
		List<WebElement> teijiButtons =webDriver.findElements(
				By.cssSelector("button.default-button"));

		for(WebElement btn : teijiButtons) {
			btn.click();
		}

		WebElement updateBtn = webDriver.findElement(By.cssSelector(".btn-info.update-button"));

		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);",updateBtn);
		Thread.sleep(300);

		((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", updateBtn);

		Alert alert = webDriver.switchTo().alert();
		alert.accept();

		getEvidence(new Object() {},"test05");

		assertTrue(webDriver.getTitle().contains("勤怠"));



	}
}
