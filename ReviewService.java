package This.is.an.execise;

import java.util.Collections;
import java.util.List;
import java.util.Map;

// declared this as a service so it can be used elsewhere
@Service
public class ReviewService extends DefaultCustomerReviewService {

	// basicly copy pasted getNumberOfReviews function with ratings
	public Integer getNumberOfReviewsRange(SessionContext ctx, Product item, int min, int max) {

		String query = "SELECT count(*) FROM {" + GeneratedCustomerReviewConstants.TC.CUSTOMERREVIEW + "} "
				+ "WHERE {product} = ?product and rating between ?min and ?max";
		Map<String, Object> values = Collections.singletonMap("product", item);
		values.put("min", min);
		values.put("max", max);
		List<Integer> result = FlexibleSearch.getInstance()
				.search(query, values, Collections.singletonList(Integer.class), true, true, 0, -1).getResult();
		return (Integer) result.iterator().next();
	}

	// reading curse words from a properties file etc.
	@Value("#{'${curse.words}'.split(',')}")
	private List<String> curseList;

	// doing the validation and then calling createCustomerReview
	public boolean createCustomerReviewAfterValidation(Double rating, String headline, String comment, UserModel user,
			ProductModel product) throws CurseWordException, RatingOutOfBoundsException {
		if (rating == null || rating < 0) {
			throw new RatingOutOfBoundsException();
		}

		for (String word : curseList) {
			if (comment.contains(word)) {
				throw new CurseWordException();
			}
		}
		createCustomerReview(rating, headline, comment, user, product);
	}

	public static class RatingOutOfBoundsException extends Exception {

	}

	public static class CurseWordException extends Exception {

	}
}
