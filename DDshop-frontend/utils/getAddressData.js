export function getAddressData({
  sameAddress,
  street,
  postalCode,
  city,
  county,
  country,
  bstreet,
  bpostalCode,
  bcity,
  bcounty,
  bcountry,
}) {
  const deliveryAddress = {
    street,
    postalCode,
    city,
    county,
    country,
  }

  const billingAddress = sameAddress
    ? deliveryAddress
    : {
        street: bstreet,
        postalCode: bpostalCode,
        city: bcity,
        county: bcounty,
        country: bcountry,
      }

  return {
    deliveryAddress,
    billingAddress,
  }
}
