package jp.co.sss.lms.ct.f03_report;

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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// TODO ここに追加
		WebElement detailBtn = webDriver.findElement(
				By.xpath("//td[contains(text(),'2022年10月2日')]/following-sibling::td//input[@value='詳細']"));

		detailBtn.click();

		getEvidence(new Object() {},"test03");

		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// TODO ここに追加
		WebElement weekReport = webDriver.findElement(
				By.cssSelector("input[type='submit'][value='提出済み週報【デモ】を確認する']"));

		weekReport.click();

		getEvidence(new Object() {},"test04");

		assertEquals("レポート登録 | LMS",webDriver.getTitle());
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() throws InterruptedException {
		// TODO ここに追加
		WebElement reportContent = webDriver.findElement(
				By.id("content_2"));

		reportContent.clear();
		reportContent.sendKeys("テスト");

		WebElement submitBtn = webDriver.findElement(
				By.cssSelector("*[type='submit'].btn-primary"));
		
		//スクロールしてボタンを視界に入れる
		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
		Thread.sleep(300);

		//強制クリック（物理クリックがブロックされるため）
		((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", submitBtn);


		getEvidence(new Object() {},"test05");

		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		// TODO ここに追加
		WebElement userDetailLink = webDriver.findElement(
				By.cssSelector("a[href='/lms/user/detail']"));
				
				userDetailLink.click();
				
				getEvidence(new Object() {},"test06");
				
				
		assertEquals("ユーザー詳細",webDriver.getTitle());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() throws InterruptedException {
		// TODO ここに追加
		WebElement detailBtn = webDriver.findElement(
			    By.xpath("//td[contains(text(),'2022年10月2日')]/following-sibling::td//input[@value='詳細']")
			);

			
			((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", detailBtn);
			Thread.sleep(300);

			
			((JavascriptExecutor)webDriver).executeScript("arguments[0].click();", detailBtn);

		getEvidence(new Object() {},"test07");

		WebElement weekReport = webDriver.findElement(
				By.xpath("//th[text()='一週間の振り返り']/following-sibling::td"));

		assertEquals("テスト",weekReport.getText());

	}
}
