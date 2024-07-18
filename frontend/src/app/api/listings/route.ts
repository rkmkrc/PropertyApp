// /pages/api/listings/route.ts
import { cookies } from 'next/headers';
import { NextRequest, NextResponse } from 'next/server';

export async function POST(req: NextRequest) {
  const { title, description, type, price, area } = await req.json();

  // Validate required fields
  if (!title || !description || !type) {
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

  const addListingUrl = `http://localhost:8080/api/v1/users/listings`;

  const response = await fetch(addListingUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: JSON.stringify({ title, description, type, price, area }),
  });

  const data = await response.json();

  if (response.ok) {
    return NextResponse.json({ success: true, message: 'Listing created successfully', data: data });
  } else {
    return NextResponse.json(
      { success: false, message: data.message || 'Listing creation failed' },
      { status: response.status }
    );
  }
}



