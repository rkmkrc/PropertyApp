import { cookies } from 'next/headers';
import { NextRequest, NextResponse } from 'next/server';

export async function POST(req: NextRequest) {
  const { type } = await req.json();

  // Validate required fields
  if (!type) {
    return NextResponse.json(
      { success: false, message: 'You need to select a package' },
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

  const addListingUrl = `http://localhost:8080/api/v1/users/packages`;

  const response = await fetch(addListingUrl, {
    method: 'POST',
    cache: "no-store",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: JSON.stringify({type}),
  });

  const data = await response.json();

  if (response.ok) {
    return NextResponse.json({ success: true, message: 'Package purchased successfully', data: data });
  } else {
    return NextResponse.json(
      { success: false, message: data.message || 'Package purchasing failed' },
      { status: response.status }
    );
  }
}