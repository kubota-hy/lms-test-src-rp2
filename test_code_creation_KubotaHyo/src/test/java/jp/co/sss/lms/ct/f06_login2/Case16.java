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
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

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
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() throws InterruptedException {
		// TODO ここに追加
		WebElement newPassword = webDriver.findElement(By.id("password"));
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		WebElement passwordChangeButton = webDriver.findElement(By.xpath("//button[@type='submit']"));

		newPassword.sendKeys("StudentAA02");
		passwordConfirm.sendKeys("StudentAA02");
		passwordChangeButton.click();

		visibilityTimeout(By.xpath("//button[@id='upd-btn']"), 2);
		WebElement changeconfirmDialog = webDriver.findElement(By.xpath("//button[@id='upd-btn']"));
		changeconfirmDialog.click();

		Thread.sleep(2000);
		
		getEvidence(new Object() {},"test04");
		
		WebElement currentPasswordError = webDriver.findElement(
				By.xpath("//label[@for='currentPassword']/following::ul[1]/li/span")
				);
		assertEquals("現在のパスワードは必須です。", currentPasswordError.getText());

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() throws InterruptedException {

		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement newPassword = webDriver.findElement(By.id("password"));
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		WebElement passwordChangeButton = webDriver.findElement(By.xpath("//button[@type='submit']"));

		// 21文字のNGパスワード
		String tooLongPass = "Aaaaaaaaaaaaaaaaaaaa1";

		currentPassword.sendKeys("StudentAA07");
		newPassword.sendKeys(tooLongPass);
		passwordConfirm.sendKeys(tooLongPass);

		passwordChangeButton.click();

		visibilityTimeout(By.id("upd-btn"), 2);
		webDriver.findElement(By.id("upd-btn")).click();

		Thread.sleep(800);
		
		getEvidence(new Object() {}, "test05");
		
		WebElement newPasswordError = webDriver.findElement(
				By.xpath("//label[@for='password']/following::ul[1]/li/span")
				);

		assertEquals("パスワードの長さが最大値(20)を超えています。", newPasswordError.getText());

		
	}


	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() throws InterruptedException {
		// TODO ここに追加 
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement newPassword = webDriver.findElement(By.id("password"));
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		WebElement passwordChangeButton = webDriver.findElement(By.xpath("//button[@type='submit']"));
		
		currentPassword.sendKeys("StudentAA07");
		newPassword.sendKeys("a");
		passwordConfirm.sendKeys("a");
		passwordChangeButton.click();
		
		visibilityTimeout(By.id("upd-btn"), 2);
		webDriver.findElement(By.id("upd-btn")).click();

		Thread.sleep(800);
		
		getEvidence(new Object() {}, "test06");
		
		WebElement newPasswordError = webDriver.findElement(
				By.xpath("//label[@for='password']/following::ul[1]/li/span")
				);

		assertEquals("「パスワード」には半角英数字のみ使用可能です。"
				+ "また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。"
				, newPasswordError.getText());

		
	
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() throws InterruptedException {
		// TODO ここに追加
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement newPassword = webDriver.findElement(By.id("password"));
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		WebElement passwordChangeButton = webDriver.findElement(By.xpath("//button[@type='submit']"));
		
		currentPassword.sendKeys("StudentAA07");
		newPassword.sendKeys("LoginId002");
		passwordConfirm.sendKeys("fjdkalsffjd");
		passwordChangeButton.click();
		
		visibilityTimeout(By.id("upd-btn"), 2);
		webDriver.findElement(By.id("upd-btn")).click();

		Thread.sleep(800);
		
		getEvidence(new Object() {}, "test07");
		
		WebElement newPasswordError = webDriver.findElement(
				By.xpath("//label[@for='password']/following::ul[1]/li/span")
				);

		assertEquals("パスワードと確認パスワードが一致しません。", newPasswordError.getText());

		
		
	}
}
