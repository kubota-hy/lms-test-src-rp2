package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト ログイン機能②
 * ケース17
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース17 受講生 初回ログイン 正常系")
public class Case17 {

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
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {
		// TODO ここに追加
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA07");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA07");
		webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(3));
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//h2[contains(text(),'利用規約')]")
				));

		getEvidence(new Object() {},"test02");

		assertTrue(webDriver.getTitle().contains("セキュリティ規約"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {
		// TODO ここに追加

		WebElement checkBox = webDriver.findElement(By.cssSelector("input[type='checkbox'][name='securityFlg']"));
		WebElement nextBtn = webDriver.findElement(By.cssSelector("button.btn.btn-primary"));

		checkBox.click();
		nextBtn.click();

		getEvidence(new Object() {},"test03");

		assertTrue(webDriver.getTitle().contains("パスワード変更"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 変更パスワードを入力し「変更」ボタン押下")
	void test04() throws InterruptedException {
		// TODO ここに追加
		
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement newPassword = webDriver.findElement(By.id("password"));
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		WebElement passwordChangeButton = webDriver.findElement(By.xpath("//button[@type='submit']"));

		currentPassword.sendKeys("StudentAA07");
		newPassword.sendKeys("LoginPass07");
		passwordConfirm.sendKeys("LoginPass07");
		passwordChangeButton.click();
		
		visibilityTimeout(By.xpath("//button[@id='upd-btn']"), 2);
		WebElement changeconfirmDialog = webDriver.findElement(By.xpath("//button[@id='upd-btn']"));
		changeconfirmDialog.click();

		Thread.sleep(2000);
		
		getEvidence(new Object() {},"test04");
		
		assertTrue(webDriver.getTitle().contains("コース詳細"));
	}

}
