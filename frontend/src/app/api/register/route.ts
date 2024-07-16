import { cookies } from 'next/headers';
import { NextResponse } from 'next/server';

export async function POST(request: Request) {
  const formData = await request.formData();
  const name = formData.get('name') as string;
  const surname = formData.get('surname') as string;
  const email = formData.get('email') as string;
  const password = formData.get('password') as string;

  // Check if all fields are filled
  if (!name || !surname || !email || !password) {
    return NextResponse.json({ success: false, message: 'All fields are required' }, { status: 400 });
  }

  // Validate email format
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    return NextResponse.json({ success: false, message: 'Please enter a valid email address' }, { status: 400 });
  }

  // Check if password is at least 6 characters long
  if (password.length < 6) {
    return NextResponse.json({ success: false, message: 'Password should be at least 6 characters long' }, { status: 400 });
  }

  // Optionally add more checks for name and surname if needed
  // Example: Ensure name and surname are alphabetic and have a minimum length
  const nameRegex = /^[A-Za-z]+$/;
  if (!nameRegex.test(name) || !nameRegex.test(surname)) {
    return NextResponse.json({ success: false, message: 'Name and surname should contain only alphabetic characters' }, { status: 400 });
  }

  const backendRegisterUrl = `http://localhost:8080/api/v1/auth/register`;

  const response = await fetch(backendRegisterUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ name, surname, email, password }),
  });

  const data = await response.json();

  if (response.ok) {
    const res = NextResponse.json({ success: true, message: 'Registration successful' });
    const oneDay = 24 * 60 * 60 * 1000
    cookies().set('token', data.token, { expires: Date.now() + oneDay })
    return res;
  } else {
    return NextResponse.json({ success: false, message: data.message || 'Registration failed' }, { status: response.status });
  }
}
