import { cookies } from 'next/headers';
import { NextRequest, NextResponse } from 'next/server';

export async function DELETE(req: NextRequest, { params }: { params: { id: string } }) {
  const { id } = params;

  if (!id) {
    return NextResponse.json(
      { success: false, message: 'Listing ID is required' },
      { status: 400 }
    );
  }

  const cookieStore = cookies();
  const token = cookieStore.get("token")?.value;

  if (!token) {
    return NextResponse.json(
      { success: false, message: 'Unauthorized' },
      { status: 401 }
    );
  }

  const deleteListingUrl = `http://localhost:8080/api/v1/users/listings/${id}`;

  const response = await fetch(deleteListingUrl, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });

  const data = await response.json();

  if (response.ok) {
    return NextResponse.json({ success: true, message: 'Listing deleted successfully', data: data });
  } else {
    return NextResponse.json(
      { success: false, message: data.message || 'Listing deletion failed' },
      { status: response.status }
    );
  }
}

export async function PUT(req: NextRequest, { params }: { params: { id: string } }) {
  const { id } = params;

  if (!id) {
    return NextResponse.json(
      { success: false, message: 'Listing ID is required' },
      { status: 400 }
    );
  }

  const listingData = await req.json();
  const { title, description, type, price, area, status } = listingData;

  // Validate required fields
  if (!title || !description || !type || !price || !area || !status) {
    return NextResponse.json(
      { success: false, message: 'All fields are required' },
      { status: 400 }
    );
  }

  // Validate price and area
  if (isNaN(price) || isNaN(area) || price <= 0 || area <= 0) {
    return NextResponse.json(
      { success: false, message: 'Price and area must be positive numbers' },
      { status: 400 }
    );
  }

  const cookieStore = cookies();
  const token = cookieStore.get("token")?.value;

  if (!token) {
    return NextResponse.json(
      { success: false, message: 'Unauthorized' },
      { status: 401 }
    );
  }

  const updateListingUrl = `http://localhost:8080/api/v1/users/listings/${id}`;

  const response = await fetch(updateListingUrl, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: JSON.stringify(listingData),
  });

  const data = await response.json();

  if (response.ok) {
    return NextResponse.json({ success: true, message: 'Listing updated successfully', data: data });
  } else {
    return NextResponse.json(
      { success: false, message: data.message || 'Listing update failed' },
      { status: response.status }
    );
  }
}