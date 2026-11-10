package com.nopcommerce.qa.tests;

import com.nopcommerce.qa.pages.CategoryPage;
import com.nopcommerce.qa.pages.HomePage;
import com.nopcommerce.qa.support.BaseTest;
import java.util.List;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CategoryNavigationTest extends BaseTest {

    private static final String PARENT_CATEGORY = "Electronics";
    private static final String CHILD_CATEGORY = "Cell phones";

    @Test(groups = "smoke", description = "TC-CAT-004: Open the selected top-level category")
    public void selectingTopLevelCategoryShowsItsHeadingAndSubcategories() {
        CategoryPage category = openElectronics();

        assertEquals(category.title(), PARENT_CATEGORY, "Unexpected top-level category heading.");
        List<String> subcategories = category.visibleSubcategoryNames();
        assertTrue(subcategories.containsAll(List.of("Camera & photo", CHILD_CATEGORY, "Others")),
                "Expected Electronics subcategories. Visible subcategories: " + subcategories);
    }

    @Test(groups = "smoke", description = "TC-CAT-005: Open the selected subcategory")
    public void selectingSubcategoryShowsItsBreadcrumbAndProductListing() {
        CategoryPage category = openElectronics().openSubcategory(CHILD_CATEGORY);

        assertEquals(category.title(), CHILD_CATEGORY, "Unexpected child category heading.");
        assertEquals(category.breadcrumb(), List.of("Home", PARENT_CATEGORY, CHILD_CATEGORY),
                "The breadcrumb should show the parent and child categories.");
        List<String> products = category.visibleProductTitles();
        assertTrue(products.contains("Apple iPhone 16 128GB"),
                "Expected the Cell phones fixture product. Visible products: " + products);
    }

    private CategoryPage openElectronics() {
        return new HomePage(driver()).open(config().baseUrl()).header().openTopLevelCategory(PARENT_CATEGORY);
    }
}
