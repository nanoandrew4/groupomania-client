package com.greenapper.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.dtos.campaign.CouponCampaignDTO;
import com.greenapper.dtos.campaign.OfferCampaignDTO;
import com.greenapper.enums.CampaignState;
import com.greenapper.enums.CampaignType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Custom deserializer for {@link CampaignDTO} subclasses, since the backend returns {@link CampaignDTO} instances,
 * and they can't be deserializer by Jackson by default.
 */
public class CampaignDTODeserializer extends StdDeserializer<CampaignDTO> {

	private Logger LOG = LoggerFactory.getLogger(CampaignDTODeserializer.class);

	public CampaignDTODeserializer() {
		super(CampaignDTO.class);
	}

	@Override
	public CampaignDTO deserialize(final JsonParser p, DeserializationContext ctxt) throws IOException {
		final JsonNode rootNode = p.getCodec().readTree(p);

		final CampaignType type = CampaignType.valueOf(rootNode.get("type").asText());

		final CampaignDTO campaignDTO;
		switch (type) {
			case OFFER:
				campaignDTO = new OfferCampaignDTO();
				break;
			case COUPON:
				campaignDTO = new CouponCampaignDTO();
				populateCouponCampaign((CouponCampaignDTO) campaignDTO, rootNode);
				break;
			default:
				LOG.error("No handling provided for campaign with type: " + type);
				campaignDTO = null;
		}

		if (campaignDTO != null)
			commonPopulate(campaignDTO, rootNode);

		return campaignDTO;
	}

	private void commonPopulate(final CampaignDTO campaignDTO, final JsonNode rootNode) {
		Optional.ofNullable(rootNode.get("id")).map(JsonNode::asLong).ifPresent(campaignDTO::setId);
		Optional.ofNullable(rootNode.get("title")).map(JsonNode::asText).ifPresent(campaignDTO::setTitle);
		Optional.ofNullable(rootNode.get("description")).map(JsonNode::asText).ifPresent(campaignDTO::setDescription);
		Optional.ofNullable(rootNode.get("campaignImageFilePath")).map(JsonNode::asText).ifPresent(campaignDTO::setCampaignImageFilePath);
		Optional.ofNullable(rootNode.get("state")).map(JsonNode::asText).map(CampaignState::valueOf).ifPresent(campaignDTO::setState);
		Optional.ofNullable(rootNode.get("startDate")).map(JsonNode::asText).ifPresent(campaignDTO::setStartDate);
		Optional.ofNullable(rootNode.get("endDate")).map(JsonNode::asText).ifPresent(campaignDTO::setEndDate);
		Optional.ofNullable(rootNode.get("quantity")).map(JsonNode::asText).ifPresent(campaignDTO::setQuantity);
		Optional.ofNullable(rootNode.get("showAfterExpiration")).map(JsonNode::asBoolean).ifPresent(campaignDTO::setShowAfterExpiration);
		Optional.ofNullable(rootNode.get("originalPrice")).map(JsonNode::asText).ifPresent(campaignDTO::setOriginalPrice);
		Optional.ofNullable(rootNode.get("percentDiscount")).map(JsonNode::asText).ifPresent(campaignDTO::setPercentDiscount);
		Optional.ofNullable(rootNode.get("discountedPrice")).map(JsonNode::asText).ifPresent(campaignDTO::setDiscountedPrice);
	}

	private void populateCouponCampaign(final CouponCampaignDTO campaignDTO, final JsonNode rootNode) {
		Optional.ofNullable(rootNode.get("couponDescription")).map(JsonNode::asText).ifPresent(campaignDTO::setCouponDescription);
		Optional.ofNullable(rootNode.get("campaignManagerName")).map(JsonNode::asText).ifPresent(campaignDTO::setCampaignManagerName);
		Optional.ofNullable(rootNode.get("campaignManagerEmail")).map(JsonNode::asText).ifPresent(campaignDTO::setCampaignManagerEmail);
		Optional.ofNullable(rootNode.get("campaignManagerAddress")).map(JsonNode::asText).ifPresent(campaignDTO::setCampaignManagerAddress);
		Optional.ofNullable(rootNode.get("couponStartDate")).map(JsonNode::asText).ifPresent(campaignDTO::setCouponStartDate);
		Optional.ofNullable(rootNode.get("couponEndDate")).map(JsonNode::asText).ifPresent(campaignDTO::setCouponEndDate);
	}
}
